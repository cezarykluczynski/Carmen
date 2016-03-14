package com.cezarykluczynski.carmen.dao.github

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.fixture.org.hibernate.SessionFactoryFixtures
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.testng.Assert

import org.joda.time.DateTimeConstants

import java.lang.reflect.Field

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class UserFollowersDAOImplTest extends AbstractTestNGSpringContextTests {

    UserFollowersDAOImpl propagationsUserFollowersDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    private SessionFactory sessionFactory

    @BeforeMethod
    void setUp() {
        propagationsUserFollowersDAOImpl = new UserFollowersDAOImpl(sessionFactory)
    }

    @Test
    void findByUser() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")

        // exercise
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findByUser userEntity

        // assertion
        Assert.assertTrue userFollowersFoundEntity instanceof UserFollowers

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
        SessionFactory sessionFactoryMock = SessionFactoryFixtures
            .createSessionFactoryMockWithEmptyCriteriaListAndMethods UserFollowers.class
        setSessionFactoryToDao propagationsUserFollowersDAOImpl, sessionFactoryMock

        // exercise
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findOldestPropagationInDiscoverPhase()

         // assertion
        Assert.assertNull userFollowersFoundEntity

        // teardown
        setSessionFactoryToDao propagationsUserFollowersDAOImpl, sessionFactory
    }

    @Test
    void create() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        // exercise
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImpl.create(userEntity, "sleep")

        // assertion
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findByUser userEntity
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
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findByUser userEntity
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

    private static void setSessionFactoryToDao(UserFollowersDAO propagationsUserFollowersDAOImpl, SessionFactory sessionFactory) {
        Field field = propagationsUserFollowersDAOImpl.getClass().getDeclaredField "sessionFactory"
        field.setAccessible true
        field.set propagationsUserFollowersDAOImpl, sessionFactory
    }

}