package com.cezarykluczynski.carmen.executor.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.client.github.GithubClient
import com.cezarykluczynski.carmen.set.github.Repository as RepositorySet
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAO

import static org.mockito.Mockito.when
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.testng.Assert

import java.util.List
import java.util.ArrayList

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class RepositoriesExecutorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    RepositoriesDAO propagationsRepositoriesDAOImpl

    @Autowired
    @InjectMocks
    RepositoriesExecutor repositoriesExecutor

    @Mock
    GithubClient githubClient

    User userEntity

    PendingRequest pendingRequestEntity

    HashMap<String, Object> pathParams

    List<RepositorySet> repositoriesSetList

    String userEntityLogin

    String mockRepositoryFullName

    @BeforeMethod
    void setUp() {
        githubClient = mock GithubClient.class
        MockitoAnnotations.initMocks this

        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userEntityLogin = userEntity.getLogin()
        mockRepositoryFullName = "${userEntityLogin}/mockRepository"

        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor "Repositories"
        pathParams = new HashMap<String, Object>()
        pathParams.put("login", userEntityLogin)
        pendingRequestEntity.setPathParams pathParams

        repositoriesSetList = new ArrayList<RepositorySet>()
        RepositorySet repositorySet = mock RepositorySet.class
        when repositorySet.getFullName() thenReturn mockRepositoryFullName
        when(githubClient.getRepositories(userEntityLogin)).thenReturn repositoriesSetList
        repositoriesSetList.add repositorySet
    }

    @Test
    void execute() {
        // exercuse
        repositoriesExecutor.execute(pendingRequestEntity)

        // assertion
        Assert.assertEquals propagationsRepositoriesDAOImpl.findByUser(userEntity).get(0).getFullName(), mockRepositoryFullName
        verify(githubClient).getRepositories(userEntityLogin)
    }


    @AfterMethod
    void tearDown() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}
