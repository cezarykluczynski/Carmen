package com.cezarykluczynski.carmen.dao.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.fixture.org.hibernate.SessionFactoryFixtures

import org.testng.annotations.Test
import org.testng.Assert

import org.joda.time.DateTimeConstants

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class UserFollowingDAOImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    @Autowired
    SessionFactoryFixtures sessionFactoryFixtures

    @Autowired
    private SessionFactory sessionFactory

    @Test
    void findByUser() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")

        // exercise
        List<UserFollowing> list = propagationsUserFollowingDAOImpl.findByUser userEntity

        // assertion
        Assert.assertEquals list.size(), 1

        // teardown
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findOldestPropagationInDiscoverPhaseExistingEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")

        Date oneDaySinceEpoch = new Date(DateTimeConstants.SECONDS_PER_DAY)
        userFollowingEntity.setUpdated oneDaySinceEpoch
        propagationsUserFollowingDAOImpl.update userFollowingEntity

        // exercise
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findOldestPropagationInDiscoverPhase()

        // assertion
        Assert.assertEquals userFollowingFoundEntity.getId(), userFollowingEntity.getId()

        // teardown
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findOldestPropagationInDiscoverPhaseNonExistingEntity() {
        // setup
        SessionFactory sessionFactoryMock = sessionFactoryFixtures
            .createSessionFactoryMockWithEmptyCriteriaListAndMethods UserFollowing.class
        propagationsUserFollowingDAOImpl.setSessionFactory sessionFactoryMock

        // exercise
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findOldestPropagationInDiscoverPhase()

         // assertion
        Assert.assertNull userFollowingFoundEntity

        // teardown
        propagationsUserFollowingDAOImpl.setSessionFactory sessionFactory
    }

    @Test
    void create() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        // exercise
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImpl.create(userEntity, "sleep")

        // assertion
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findByUser(userEntity).get 0
        Assert.assertEquals userFollowingFoundEntity.getId(), userFollowingEntity.getId()
        Assert.assertEquals userFollowingFoundEntity.getPhase(), "sleep"

        // teardown
        propagationsUserFollowingDAOImpl.delete userFollowingEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void update() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImpl.create(userEntity, "sleep")
        userFollowingEntity.setPhase "discover"

        // exercise
        propagationsUserFollowingDAOImpl.update userFollowingEntity

        // assertion
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findByUser(userEntity).get 0
        Assert.assertEquals userFollowingFoundEntity.getPhase(), "discover"

        // teardown
        propagationsUserFollowingDAOImpl.delete userFollowingEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void delete() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImpl.create(userEntity, "sleep")

        // exercise
        propagationsUserFollowingDAOImpl.delete userFollowingEntity

        // assertion
        Assert.assertNull propagationsUserFollowingDAOImpl.findById(userFollowingEntity.getId())

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findByIdExistingEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImpl.create(userEntity, "sleep")

        // exercise
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findById(userFollowingEntity.getId())

        // assertion
        Assert.assertTrue userFollowingFoundEntity instanceof UserFollowing

        // teardown
        propagationsUserFollowingDAOImpl.delete userFollowingEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findByIdNonExistingEntity() {
        // exercise
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findById 2147483647

        // assertion
        Assert.assertNull userFollowingFoundEntity
    }

}
