package com.cezarykluczynski.carmen.vcs.git.worker

import com.cezarykluczynski.carmen.dao.github.RepositoriesClonesDAO
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.util.DateUtil
import com.cezarykluczynski.carmen.util.exec.Command
import com.cezarykluczynski.carmen.util.exec.Executor
import com.cezarykluczynski.carmen.util.exec.Result
import com.cezarykluczynski.carmen.util.filesystem.Directory
import com.cezarykluczynski.carmen.vcs.server.Server
import com.cezarykluczynski.carmen.vcs.server.ServerTest
import lombok.extern.java.Log
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
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
        println "now: " + now
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
        println "localRepositoryCanBeCloned start"
        gitHubCloneWorker.run()

        // assertion
        RepositoryClone repositoryCloneEntityResult = repositoriesClonesDAO.findByRepositoryEntity repositoryEntity
        println "localRepositoryCanBeCloned id: " + repositoryCloneEntityResult.getId()
        println "localRepositoryCanBeCloned cloned: " + repositoryCloneEntityResult.getCloned()
        Assert.assertEquals repositoryCloneEntityResult.getServerId(), server.getServerId()
        Assert.assertTrue repositoryCloneEntityResult.getCloned().equals(now) || repositoryCloneEntityResult.getCloned().after(now)

        String cloneDirectory = "${server.getCloneRoot()}/${repositoryCloneEntityResult.getLocationDirectory()}/" +
        "${repositoryCloneEntityResult.getLocationSubdirectory()}"
        String revParseCommandBody = "git rev-parse --resolve-git-dir ${cloneDirectory}/.git"

        Command revParseCommand = new Command(revParseCommandBody)
        Result revParseCommandResult = Executor.execute(revParseCommand)
        Assert.assertTrue revParseCommandResult.isSuccessFull()
        Assert.assertTrue revParseCommandResult.getOutput().contains(repositoryEntity.getFullName())

        // teardown
        repositoriesDAOImplFixtures.deleteRepositoryEntity repositoryEntity
        println "localRepositoryCanBeCloned stop"
    }

    @Test
    void invalidRepositoryCannotBeCloned() {
        println "invalidRepositoryCannotBeCloned start"
        // setup
        repositoryEntity.setCloneUrl server.getCloneRoot()

        // exercise
        gitHubCloneWorker.run()

        // assertion
        RepositoryClone repositoryCloneEntityResult = repositoriesClonesDAO.findByRepositoryEntity repositoryEntity
        println "invalidRepositoryCannotBeCloned id: " + repositoryCloneEntityResult.getId()
        Assert.assertNull repositoryCloneEntityResult.getServerId()
        Assert.assertNull repositoryCloneEntityResult.getCloned()

        // teardown
        repositoriesDAOImplFixtures.deleteRepositoryEntity repositoryEntity
        println "invalidRepositoryCannotBeCloned stop"
    }

    @AfterMethod
    void tearDown() {
        userDAOImplFixtures.deleteUserEntity userEntity
        Directory.delete server.getCloneRoot()
    }

}
