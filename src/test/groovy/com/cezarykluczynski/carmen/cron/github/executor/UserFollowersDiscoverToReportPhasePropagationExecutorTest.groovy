package com.cezarykluczynski.carmen.cron.github.executor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowers

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.testng.Assert

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserFollowersDiscoverToReportPhasePropagationExecutorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowersDAO propagationsUserFollowersDAOImpl

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    PendingRequestDAOImplFixtures apiqueuePendingRequestDAOImplFixtures

    @Autowired
    //@InjectMocks
    UserFollowersDiscoverToReportPhasePropagationExecutor userFollowersDiscoverToReportPhasePropagationExecutor

    User userEntity

    UserFollowers userFollowersEntity

    @BeforeMethod
    public void setUp() {
        //MockitoAnnotations.initMocks this
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
    }

    @Test
    void tryToMoveToReportPhaseNoPendingRequestDiscoverPhase() {
        // setup
        userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowersEntity.setUpdated(new Date(0L))
        propagationsUserFollowersDAOImpl.update userFollowersEntity

        // exercise
        userFollowersDiscoverToReportPhasePropagationExecutor.run()

        // assertion
        UserFollowers propagationsUserFollowersEntity = propagationsUserFollowersDAOImpl.findByUser userEntity
        Assert.assertEquals propagationsUserFollowersEntity.getPhase(), "report"

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
    }

    @Test
    void tryToMoveToReportPhaseNoPendingRequestSleepPhase() {
        // setup
        userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "sleep")
        userFollowersEntity.setUpdated(new Date(0L))
        propagationsUserFollowersDAOImpl.update userFollowersEntity

        // exercise
        userFollowersDiscoverToReportPhasePropagationExecutor.run()

        // assertion
        UserFollowers propagationsUserFollowersEntity = propagationsUserFollowersDAOImpl.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowersEntity.getPhase(), "sleep"

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
    }

    @Test
    void tryToMoveToReportPhasePendingRequestDiscoverPhase() {
        // setup
        userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowersEntity.setUpdated(new Date(0L))
        propagationsUserFollowersDAOImpl.update userFollowersEntity
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndUserFollowersEntity(
                userEntity, userFollowersEntity
            )
        pendingRequestEntity.setPropagationId userFollowersEntity.getId()

        // exercise
        userFollowersDiscoverToReportPhasePropagationExecutor.run()

        // assertion
        UserFollowers propagationsUserFollowersEntity = propagationsUserFollowersDAOImpl.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowersEntity.getPhase(), "discover"

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        apiqueuePendingRequestDAOImplFixtures.deletePendingRequestEntity pendingRequestEntity
    }

    @AfterMethod
    void tearDown() {
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntityByUserEntity userEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}
