package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.AfterMethod
import org.testng.annotations.Test
import org.testng.Assert

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class UserFollowingPropagationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowingPropagation userFollowingPropagation

    @Autowired
    UserFollowingDAO propagationsUserFollowingDAOImpl

    @Autowired
    UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    User userEntity

    @Test
    void propagateNotFoundEntity() {
        // setup
        userEntity = githubUserDAOImplFixtures.createNotFoundRequestedUserEntity()
        userFollowingPropagation.setUserEntity userEntity

        // exercise
        userFollowingPropagation.propagate()

        // assertion
        Assert.assertNull propagationsUserFollowingDAOImpl.findByUser(userEntity)
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
        UserFollowing propagationsUserFollowingEntity = propagationsUserFollowingDAOImpl.findByUser(userEntity)
        Assert.assertTrue propagationsUserFollowingEntity instanceof UserFollowing
        Assert.assertEquals propagationsUserFollowingEntity.getId(), userFollowingEntity.getId()
        Assert.assertEquals propagationsUserFollowingEntity.getPhase(), userFollowingEntity.getPhase()

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
        UserFollowing propagationsUserFollowingEntity = propagationsUserFollowingDAOImpl.findByUser(userEntity)
        Assert.assertTrue propagationsUserFollowingEntity instanceof UserFollowing
        Assert.assertEquals propagationsUserFollowingEntity.getPhase(), "discover"
    }

    @AfterMethod
    void tearDown() {
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntityByUserEntity userEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}
