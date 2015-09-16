package com.cezarykluczynski.carmen.executor.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest

import static org.mockito.Mockito.when
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.verify
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserGhostExecutorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    @InjectMocks
    UserGhostExecutor userGhostExecutor

    @Mock
    UserDAOImpl githubUserDAOImpl

    User userEntity1

    User userEntity2

    PendingRequest pendingRequestEntity

    HashMap<String, Object> params

    HashMap<String, Object> pathParams

    @BeforeMethod
    void setUp() {
        githubUserDAOImpl = mock UserDAOImpl.class
        MockitoAnnotations.initMocks this

        userEntity1 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userEntity2 = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        pendingRequestEntity = new PendingRequest()
        pendingRequestEntity.setExecutor "UsersGhost"

        params = new HashMap<String, Object>()
        params.put("link_with", userEntity2.getId())

        pathParams = new HashMap<String, Object>()
        pathParams.put("login", userEntity1.getLogin())
        pendingRequestEntity.setPathParams pathParams
    }

    @Test
    void executeFollower() {
        // setup
        params.put("link_as", "follower")
        pendingRequestEntity.setParams params

        doNothing().when(githubUserDAOImpl).linkFollowerWithFollowee(userEntity1, userEntity2)
        when githubUserDAOImpl.createOrUpdateGhostEntity(userEntity1.getLogin()) thenReturn userEntity1
        when githubUserDAOImpl.findById(userEntity2.getId().intValue()) thenReturn userEntity2

        // exercise
        userGhostExecutor.execute pendingRequestEntity

        // assertion
        verify(githubUserDAOImpl).linkFollowerWithFollowee(userEntity1, userEntity2)
    }

    @Test
    void executeFollowee() {
        // setup
        params.put("link_as", "followee")
        pendingRequestEntity.setParams params
        pendingRequestEntity.setPathParams pathParams

        doNothing().when(githubUserDAOImpl).linkFollowerWithFollowee(userEntity2, userEntity1)
        when githubUserDAOImpl.createOrUpdateGhostEntity(userEntity1.getLogin()) thenReturn userEntity1
        when githubUserDAOImpl.findById(userEntity2.getId().intValue()) thenReturn userEntity2

        // exercise
        userGhostExecutor.execute pendingRequestEntity

        // assertion
        verify(githubUserDAOImpl).linkFollowerWithFollowee(userEntity2, userEntity1)
    }

    @AfterMethod
    void tearDown() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity1
        githubUserDAOImplFixtures.deleteUserEntity userEntity2
    }

}
