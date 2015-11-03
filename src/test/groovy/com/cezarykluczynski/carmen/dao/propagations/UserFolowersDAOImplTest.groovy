package com.cezarykluczynski.carmen.dao.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
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
class UserFollowersDAOImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserFollowersDAO propagationsUserFollowersDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    SessionFactoryFixtures sessionFactoryFixtures

    @Autowired
    private SessionFactory sessionFactory

    @Test
    void findByUser() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")

        // exercise
        List<UserFollowers> list = propagationsUserFollowersDAOImpl.findByUser userEntity

        // assertion
        Assert.assertEquals list.size(), 1

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findOldestPropagationInDiscoverPhaseExistingEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")

        Date oneDaySinceEpoch = new Date(DateTimeConstants.SECONDS_PER_DAY)
        userFollowersEntity.setUpdated oneDaySinceEpoch
        propagationsUserFollowersDAOImpl.update userFollowersEntity

        // exercise
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findOldestPropagationInDiscoverPhase()

        // assertion
        Assert.assertEquals userFollowersFoundEntity.getId(), userFollowersEntity.getId()

        // teardown
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findOldestPropagationInDiscoverPhaseNonExistingEntity() {
        // setup
        SessionFactory sessionFactoryMock = sessionFactoryFixtures
            .createSessionFactoryMockWithEmptyCriteriaListAndMethods UserFollowers.class
        propagationsUserFollowersDAOImpl.setSessionFactory sessionFactoryMock

        // exercise
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findOldestPropagationInDiscoverPhase()

         // assertion
        Assert.assertNull userFollowersFoundEntity

        // teardown
        propagationsUserFollowersDAOImpl.setSessionFactory sessionFactory
    }

    @Test
    void create() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        // exercise
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImpl.create(userEntity, "sleep")

        // assertion
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findByUser(userEntity).get 0
        Assert.assertEquals userFollowersFoundEntity.getId(), userFollowersEntity.getId()
        Assert.assertEquals userFollowersFoundEntity.getPhase(), "sleep"

        // teardown
        propagationsUserFollowersDAOImpl.delete userFollowersEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void update() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImpl.create(userEntity, "sleep")
        userFollowersEntity.setPhase "discover"

        // exercise
        propagationsUserFollowersDAOImpl.update userFollowersEntity

        // assertion
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findByUser(userEntity).get 0
        Assert.assertEquals userFollowersFoundEntity.getPhase(), "discover"

        // teardown
        propagationsUserFollowersDAOImpl.delete userFollowersEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void delete() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImpl.create(userEntity, "sleep")

        // exercise
        propagationsUserFollowersDAOImpl.delete userFollowersEntity

        // assertion
        Assert.assertNull propagationsUserFollowersDAOImpl.findById(userFollowersEntity.getId())

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findByIdExistingEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImpl.create(userEntity, "sleep")

        // exercise
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findById(userFollowersEntity.getId())

        // assertion
        Assert.assertTrue userFollowersFoundEntity instanceof UserFollowers

        // teardown
        propagationsUserFollowersDAOImpl.delete userFollowersEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findByIdNonExistingEntity() {
        // exercise
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findById 2147483647

        // assertion
        Assert.assertNull userFollowersFoundEntity
    }

}
