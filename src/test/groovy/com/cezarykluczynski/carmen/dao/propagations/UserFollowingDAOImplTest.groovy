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
    UserFollowingDAOImpl propagationsUserFollowingDao

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
        List<UserFollowing> list = propagationsUserFollowingDao.findByUser userEntity

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
        propagationsUserFollowingDao.update userFollowingEntity

        // exercise
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDao.findOldestPropagationInDiscoverPhase()

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
        propagationsUserFollowingDao.setSessionFactory sessionFactoryMock

        // exercise
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDao.findOldestPropagationInDiscoverPhase()

         // assertion
        Assert.assertNull userFollowingFoundEntity

        // teardown
        propagationsUserFollowingDao.setSessionFactory sessionFactory
    }

    @Test
    void create() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        // exercise
        UserFollowing userFollowingEntity = propagationsUserFollowingDao.create(userEntity, "sleep")

        // assertion
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDao.findByUser(userEntity).get 0
        Assert.assertEquals userFollowingFoundEntity.getId(), userFollowingEntity.getId()
        Assert.assertEquals userFollowingFoundEntity.getPhase(), "sleep"

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        propagationsUserFollowingDao.delete userFollowingEntity
    }

    @Test
    void update() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDao.create(userEntity, "sleep")
        userFollowingEntity.setPhase "discover"

        // exercise
        propagationsUserFollowingDao.update userFollowingEntity

        // assertion
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDao.findByUser(userEntity).get 0
        Assert.assertEquals userFollowingFoundEntity.getPhase(), "discover"

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        propagationsUserFollowingDao.delete userFollowingEntity
    }

    @Test
    void delete() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDao.create(userEntity, "sleep")

        // exercise
        propagationsUserFollowingDao.delete userFollowingEntity

        // assertion
        Assert.assertNull propagationsUserFollowingDao.findById(userFollowingEntity.getId())

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findByIdExistingEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDao.create(userEntity, "sleep")

        // exercise
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDao.findById(userFollowingEntity.getId())

        // assertion
        Assert.assertTrue userFollowingFoundEntity instanceof UserFollowing

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        propagationsUserFollowingDao.delete userFollowingEntity
    }

    @Test
    void findByIdNonExistingEntity() {
        // exercise
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDao.findById 2147483647

        // assertion
        Assert.assertNull userFollowingFoundEntity
    }

}
