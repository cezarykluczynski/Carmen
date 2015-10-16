package com.cezarykluczynski.carmen.propagation

import com.cezarykluczynski.carmen.propagation.github.UserFollowingPropagation
import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImplFixtures
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowing

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
class UserFollowingPropagationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowingPropagation userFollowingPropagation

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDao

    @Autowired
    UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    @Autowired
    PendingRequestDAOImplFixtures apiqueuePendingRequestDAOImplFixtures

    User userEntity

    @Test
    void propagateNotFoundEntity() {
        // setup
        userEntity = githubUserDAOImplFixtures.createNotFoundRequestedUserEntity()
        userFollowingPropagation.setUserEntity userEntity

        // exercise
        userFollowingPropagation.propagate()

        // assertion
        Assert.assertEquals propagationsUserFollowingDao.findByUser(userEntity).size(), 0
    }

    @Test
    void propagateNonEmptyPropagations() {
        // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")

        userFollowingPropagation.setUserEntity userEntity

        // exercise
        userFollowingPropagation.propagate()

        // assertion: another propagation should not be created
        List<UserFollowing> propagationsUserFollowingDAOImplList = propagationsUserFollowingDao.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowingDAOImplList.size(), 1
        Assert.assertEquals propagationsUserFollowingDAOImplList.get(0).getId(), userFollowingEntity.getId()
        Assert.assertEquals propagationsUserFollowingDAOImplList.get(0).getPhase(), userFollowingEntity.getPhase()

        // teardown
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
    }

    @Test
    void propagateFoundEntity() {
        // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userFollowingPropagation.setUserEntity userEntity

        // exercise
        userFollowingPropagation.propagate()

        // assertion
        List<UserFollowing> propagationsUserFollowingDAOImplList = propagationsUserFollowingDao.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowingDAOImplList.size(), 1
        Assert.assertEquals propagationsUserFollowingDAOImplList.get(0).getPhase(), "discover"
    }

    @Test
    void tryToMoveToReportPhaseNoPendingRequestDiscoverPhase() {
        // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowingPropagation.setUserEntity userEntity

        // exercise
        userFollowingPropagation.tryToMoveToReportPhase userFollowingEntity.getId()

        // assertion
        List<UserFollowing> propagationsUserFollowingDAOImplList = propagationsUserFollowingDao.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowingDAOImplList.get(0).getPhase(), "report"

        // teardown
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
    }

    @Test
    void tryToMoveToReportPhaseNoPendingRequestSleepPhase() {
        // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "sleep")
        userFollowingPropagation.setUserEntity userEntity

        // exercise
        userFollowingPropagation.tryToMoveToReportPhase userFollowingEntity.getId()

        // assertion
        List<UserFollowing> propagationsUserFollowingDAOImplList = propagationsUserFollowingDao.findByUser(userEntity)
        Assert.assertEquals propagationsUserFollowingDAOImplList.get(0).getPhase(), "sleep"

        // teardown
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
    }

    @Test
    void tryToMoveToReportPhasePendingRequestDiscoverPhase() {
        // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowingPropagation.setUserEntity userEntity
        PendingRequest pendingRequestEntity =
            apiqueuePendingRequestDAOImplFixtures.createPendingRequestEntityUsingUserEntityAndUserFollowingEntity(
                userEntity, userFollowingEntity
            )
        pendingRequestEntity.setPropagationId userFollowingEntity.getId()

        // exercise
        userFollowingPropagation.tryToMoveToReportPhase pendingRequestEntity

        // assertion
        List<UserFollowing> propagationsUserFollowingDAOImplList = propagationsUserFollowingDao.findByUser(userEntity)
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
