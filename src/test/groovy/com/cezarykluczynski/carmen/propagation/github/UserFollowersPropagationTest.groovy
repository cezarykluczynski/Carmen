package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.propagation.github.UserFollowersPropagation
import org.hibernate.Session
import org.hibernate.SessionFactory

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
class UserFollowersPropagationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowersPropagation userFollowersPropagation

    @Autowired
    UserFollowersDAO propagationsUserFollowersDAOImpl

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
        Assert.assertNull propagationsUserFollowersDAOImpl.findByUser(userEntity)
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
        UserFollowers propagationsUserFollowersEntity = propagationsUserFollowersDAOImpl.findByUser(userEntity)
        Assert.assertTrue propagationsUserFollowersEntity instanceof UserFollowers
        Assert.assertEquals propagationsUserFollowersEntity.getId(), userFollowersEntity.getId()
        Assert.assertEquals propagationsUserFollowersEntity.getPhase(), userFollowersEntity.getPhase()

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
        UserFollowers propagationsUserFollowersEntity = propagationsUserFollowersDAOImpl.findByUser(userEntity)
        Assert.assertTrue propagationsUserFollowersEntity instanceof UserFollowers
        Assert.assertEquals propagationsUserFollowersEntity.getPhase(), "discover"
    }

    @AfterMethod
    void tearDown() {
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntityByUserEntity userEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}
