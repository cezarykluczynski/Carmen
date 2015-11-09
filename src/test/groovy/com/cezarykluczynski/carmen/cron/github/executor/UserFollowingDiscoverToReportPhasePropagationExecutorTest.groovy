package com.cezarykluczynski.carmen.cron.github.executor

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowing

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.testng.Assert

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import org.mockito.Mock
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserFollowingDiscoverToReportPhasePropagationExecutorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowingDAO propagationsUserFollowingDAOImpl

    @Autowired
    UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    @Autowired
    PendingRequestDAOImplFixtures apiqueuePendingRequestDAOImplFixtures

    @Autowired
    //@InjectMocks
    UserFollowingDiscoverToReportPhasePropagationExecutor userFollowingDiscoverToReportPhasePropagationExecutor

    User userEntity

    UserFollowing userFollowingEntity

    @BeforeMethod
    public void setUp() {
        //MockitoAnnotations.initMocks this
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
    }

    @Test
    void tryToMoveToReportPhaseNoPendingRequestDiscoverPhase() {
        // setup
        userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowingEntity.setUpdated(new Date(0L))
        propagationsUserFollowingDAOImpl.update userFollowingEntity

        // exercise
        userFollowingDiscoverToReportPhasePropagationExecutor.run()

        // assertion
        List<UserFollowing> propagationsUserFollowingDAOImplList = propagationsUserFollowingDAOImpl.findByUser userEntity
        Assert.assertEquals propagationsUserFollowingDAOImplList.get(0).getPhase(), "report"

        // teardown
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
    }

    @Test
    void tryToMoveToReportPhaseNoPendingRequestSleepPhase() {
        // setup
        userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "sleep")
        userFollowingEntity.setUpdated(new Date(0L))
        propagationsUserFollowingDAOImpl.update userFollowingEntity

        // exercise
        userFollowingDiscoverToReportPhasePropagationExecutor.run()

        // assertion
        List<UserFollowing> propagationsUserFollowingDAOImplList = propagationsUserFollowingDAOImpl.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowingDAOImplList.get(0).getPhase(), "sleep"

        // teardown
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
    }

    @Test
    void tryToMoveToReportPhasePendingRequestDiscoverPhase() {
        // setup
        userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowingEntity.setUpdated(new Date(0L))
        propagationsUserFollowingDAOImpl.update userFollowingEntity
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndUserFollowingEntity(
                userEntity, userFollowingEntity
            )
        pendingRequestEntity.setPropagationId userFollowingEntity.getId()

        // exercise
        userFollowingDiscoverToReportPhasePropagationExecutor.run()

        // assertion
        List<UserFollowing> propagationsUserFollowingDAOImplList = propagationsUserFollowingDAOImpl.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowingDAOImplList.get(0).getPhase(), "discover"

        // teardown
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
    }

    @AfterMethod
    void tearDown() {
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntityByUserEntity userEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}
