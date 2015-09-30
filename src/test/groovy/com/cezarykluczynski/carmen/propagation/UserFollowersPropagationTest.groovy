package com.cezarykluczynski.carmen.propagation

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl
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
class UserFollowersPropagationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowersPropagation userFollowersPropagation

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDao

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    PendingRequestDAOImplFixtures apiqueuePendingRequestDAOImplFixtures

    User userEntity

    @Test
    void propagateNotFoundEntity() {
        // setup
        userEntity = githubUserDAOImplFixtures.createNotFoundRequestedUserEntity()
        userFollowersPropagation.setUserEntity userEntity

        // exercise
        userFollowersPropagation.propagate()

        // assertion
        Assert.assertEquals propagationsUserFollowersDao.findByUser(userEntity).size(), 0
    }

    @Test
    void propagateNonEmptyPropagations() {
        // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")

        userFollowersPropagation.setUserEntity userEntity

        // exercise
        userFollowersPropagation.propagate()

        // assertion: another propagation should not be created
        List<UserFollowers> propagationsUserFollowersDAOImplList = propagationsUserFollowersDao.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowersDAOImplList.size(), 1
        Assert.assertEquals propagationsUserFollowersDAOImplList.get(0).getId(), userFollowersEntity.getId()
        Assert.assertEquals propagationsUserFollowersDAOImplList.get(0).getPhase(), userFollowersEntity.getPhase()

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
    }

    @Test
    void propagateFoundEntity() {
        // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userFollowersPropagation.setUserEntity userEntity

        // exercise
        userFollowersPropagation.propagate()

        // assertion
        List<UserFollowers> propagationsUserFollowersDAOImplList = propagationsUserFollowersDao.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowersDAOImplList.size(), 1
        Assert.assertEquals propagationsUserFollowersDAOImplList.get(0).getPhase(), "discover"


    }

    @Test
    void tryToMoveToReportPhaseNoPendingRequestDiscoverPhase() {
        // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowersPropagation.setUserEntity userEntity

        // exercise
        userFollowersPropagation.tryToMoveToReportPhase userFollowersEntity.getId()

        // assertion
        List<UserFollowers> propagationsUserFollowersDAOImplList = propagationsUserFollowersDao.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowersDAOImplList.get(0).getPhase(), "report"

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
    }

    @Test
    void tryToMoveToReportPhaseNoPendingRequestSleepPhase() {
        // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "sleep")
        userFollowersPropagation.setUserEntity userEntity

        // exercise
        userFollowersPropagation.tryToMoveToReportPhase userFollowersEntity.getId()

        // assertion
        List<UserFollowers> propagationsUserFollowersDAOImplList = propagationsUserFollowersDao.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowersDAOImplList.get(0).getPhase(), "sleep"

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
    }

    @Test
    void tryToMoveToReportPhasePendingRequestDiscoverPhase() {
        // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowersPropagation.setUserEntity userEntity
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndUserFollowersEntity(
                userEntity, userFollowersEntity
            )
        pendingRequestEntity.setPropagationId userFollowersEntity.getId()

        // exercise
        userFollowersPropagation.tryToMoveToReportPhase pendingRequestEntity

        // assertion
        List<UserFollowers> propagationsUserFollowersDAOImplList = propagationsUserFollowersDao.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowersDAOImplList.get(0).getPhase(), "discover"

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
