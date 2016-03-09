package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.cassandra.github_social_stats.FollowersAndFollowees
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
import com.cezarykluczynski.carmen.repository.githubstats.FollowersAndFolloweesRepository
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.testng.Assert

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class UserFollowersFollowingReportToSleepPhasePropagationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase

    @Autowired
    FollowersAndFolloweesRepository followersAndFolloweesRepository

    @Autowired
    UserFollowersDAO propagationsUserFollowersDAOImpl

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    UserFollowingDAO propagationsUserFollowingDAOImpl

    @Autowired
    UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    User userEntity

    UserFollowers userFollowersEntity

    UserFollowing userFollowingEntity

    @BeforeMethod
    void setUp() {
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        propagationUserFollowersFollowingReportToSleepPhase.setUserEntity userEntity
        userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase userEntity, "report"
        userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase userEntity, "report"
    }

    @Test
    void propagateNonExistingFollowersAndFolloweesEntity() {
        // exercise
        propagationUserFollowersFollowingReportToSleepPhase.propagate()

        // assertion
        Assert.assertEquals propagationsUserFollowersDAOImpl.findById(userFollowersEntity.getId()).getPhase(), "sleep"
        Assert.assertEquals propagationsUserFollowingDAOImpl.findById(userFollowingEntity.getId()).getPhase(), "sleep"
        Assert.assertTrue followersAndFolloweesRepository.findByUserId(userEntity.getId()) instanceof FollowersAndFollowees
    }

    @Test
    void propagateExistingFollowersAndFolloweesEntity() {
        // setup
        FollowersAndFollowees followersAndFolloweesEntity = new FollowersAndFollowees()
        followersAndFolloweesEntity.setId()
        followersAndFolloweesEntity.setUserId userEntity.getId()
        followersAndFolloweesRepository.save followersAndFolloweesEntity

        // exercise
        propagationUserFollowersFollowingReportToSleepPhase.propagate()

        // assertion
        Assert.assertEquals propagationsUserFollowersDAOImpl.findById(userFollowersEntity.getId()).getPhase(), "sleep"
        Assert.assertEquals propagationsUserFollowingDAOImpl.findById(userFollowingEntity.getId()).getPhase(), "sleep"
        Assert.assertTrue followersAndFolloweesRepository.findByUserId(userEntity.getId()) instanceof FollowersAndFollowees
    }

    @AfterMethod
    void tearDown() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}
