package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.propagation.github.UserFollowersFollowingReportToSleepPhasePropagation
import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.githubstats.FollowersAndFollowees
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
import com.cezarykluczynski.carmen.repository.githubstats.FollowersAndFolloweesRepository

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

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserFollowersFollowingReportToSleepPhasePropagationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase

    @Autowired
    FollowersAndFolloweesRepository followersAndFolloweesRepository

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDao

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDao

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
        Assert.assertEquals propagationsUserFollowersDao.findById(userFollowersEntity.getId()).getPhase(), "sleep"
        Assert.assertEquals propagationsUserFollowingDao.findById(userFollowingEntity.getId()).getPhase(), "sleep"
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
        Assert.assertEquals propagationsUserFollowersDao.findById(userFollowersEntity.getId()).getPhase(), "sleep"
        Assert.assertEquals propagationsUserFollowingDao.findById(userFollowingEntity.getId()).getPhase(), "sleep"
        Assert.assertTrue followersAndFolloweesRepository.findByUserId(userEntity.getId()) instanceof FollowersAndFollowees
    }

    @AfterMethod
    void tearDown() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
    }

}
