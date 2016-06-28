package com.cezarykluczynski.carmen.vcs.git.worker

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.dao.github.RepositoriesClonesDAO
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.util.DateUtil
import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand
import com.cezarykluczynski.carmen.util.exec.command.Command
import com.cezarykluczynski.carmen.util.exec.executor.Executor
import com.cezarykluczynski.carmen.util.exec.result.Result
import com.cezarykluczynski.carmen.util.filesystem.Directory
import com.cezarykluczynski.carmen.vcs.server.Server
import com.cezarykluczynski.carmen.vcs.server.ServerTest
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Matchers.isA
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class GitHubCloneWorkerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures userDAOImplFixtures

    @Autowired
    RepositoriesDAOImplFixtures repositoriesDAOImplFixtures

    @Autowired
    RepositoriesClonesDAO repositoriesClonesDAO

    @Mock
    RepositoriesDAO repositoriesDAO

    @Mock
    Server server

    @Autowired
    @InjectMocks
    GitHubCloneWorker gitHubCloneWorker

    User userEntity

    Repository repositoryEntity

    Date now

    @BeforeClass
    void setupClass() {
        now = DateUtil.now()
    }

    @BeforeMethod
    void setup() {
        repositoriesDAO = mock RepositoriesDAO.class
        server = mock Server.class
        MockitoAnnotations.initMocks this

        userEntity = userDAOImplFixtures.createFoundRequestedUserEntity()
        repositoryEntity = repositoriesDAOImplFixtures.createRandomEntityUsingUserEntity userEntity
        repositoryEntity.setCloneUrl "."
        repositoryEntity.setFullName "test/test"

        when server.getCloneRoot() thenReturn ServerTest.CLONE_ROOT
        when repositoriesDAO.findNotForkedRepositoryWithoutClone() thenReturn repositoryEntity
    }

    @Test
    void localRepositoryCanBeCloned() {
        // exercise
        gitHubCloneWorker.run()

        // assertion
        RepositoryClone repositoryCloneEntityResult = repositoriesClonesDAO.findByRepositoryEntity repositoryEntity
        Assert.assertEquals repositoryCloneEntityResult.getServerId(), server.getServerId()
        Assert.assertTrue repositoryCloneEntityResult.getCloned().getTime() >= now.getTime()

        String cloneDirectory = "${server.getCloneRoot()}/${repositoryCloneEntityResult.getLocationDirectory()}/" +
        "${repositoryCloneEntityResult.getLocationSubdirectory()}"
        String revParseCommandBody = "git rev-parse --resolve-git-dir ${cloneDirectory}/.git"

        Command revParseCommand = new ApacheCommonsCommand(revParseCommandBody)
        Result revParseCommandResult = Executor.execute(revParseCommand)
        Assert.assertTrue revParseCommandResult.isSuccessFul()
        Assert.assertTrue revParseCommandResult.getOutput().contains(repositoryEntity.getFullName())

        // teardown
        repositoriesDAOImplFixtures.deleteRepositoryEntity repositoryEntity
    }

    @Test
    void nullRepositoryEntity() {
        // setup
        RepositoriesClonesDAO repositoriesClonesDAO = mock RepositoriesClonesDAO.class
        when(repositoriesClonesDAO.createStubEntity(isA(Server.class), isA(Repository.class))).thenReturn null
        RepositoriesDAO repositoriesDAO = mock RepositoriesDAO
        when repositoriesDAO.findNotForkedRepositoryWithoutClone() thenReturn null
        GitHubCloneWorker gitHubCloneWorker = new GitHubCloneWorker(repositoriesDAO, repositoriesClonesDAO, server)

        // exercise
        gitHubCloneWorker.run()

        // assertion
        verify(repositoriesClonesDAO, never()).createStubEntity(isA(Server.class), isA(Repository.class))
    }

    @Test
    void invalidRepositoryCannotBeCloned() {
        // setup
        repositoryEntity.setCloneUrl server.getCloneRoot()

        // exercise
        gitHubCloneWorker.run()

        // assertion
        RepositoryClone repositoryCloneEntityResult = repositoriesClonesDAO.findByRepositoryEntity repositoryEntity
        Assert.assertNull repositoryCloneEntityResult.getServerId()
        Assert.assertNull repositoryCloneEntityResult.getCloned()

        // teardown
        repositoriesDAOImplFixtures.deleteRepositoryEntity repositoryEntity
    }

    @Test
    void invalidCloneRepositoryDoesNotMakeClone() {
        // setup
        RepositoriesClonesDAO repositoriesClonesDAO = mock RepositoriesClonesDAO.class
        doNothing().when(repositoriesClonesDAO).setStatusToCloned(isA(RepositoryClone.class))
        when(repositoriesClonesDAO.truncateEntity(isA(Server.class), isA(RepositoryClone.class))).thenReturn null
        GitHubCloneWorker gitHubCloneWorker = new GitHubCloneWorker(repositoriesDAO, repositoriesClonesDAO, server)
        when repositoriesClonesDAO.createStubEntity(server, repositoryEntity) thenReturn null

        // exercise
        gitHubCloneWorker.run()

        verify(repositoriesClonesDAO, never()).setStatusToCloned(isA(RepositoryClone.class))
        verify(repositoriesClonesDAO, never()).truncateEntity(isA(Server.class), isA(RepositoryClone.class))
    }

    @AfterMethod
    void tearDown() {
        userDAOImplFixtures.deleteUserEntity userEntity
        Directory.delete server.getCloneRoot()
    }

}
