package com.cezarykluczynski.carmen.dao.propagations

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.propagations.UserFollowing
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
class UserFollowingDAOImplTest extends AbstractTestNGSpringContextTests {

    UserFollowingDAOImpl propagationsUserFollowingDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    @Autowired
    private SessionFactory sessionFactory


    @BeforeMethod
    void setUp() {
        propagationsUserFollowingDAOImpl = new UserFollowingDAOImpl(sessionFactory)
    }

    @Test
    void findByUser() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")

        // exercise
        UserFollowing userFollowingEntityFound = propagationsUserFollowingDAOImpl.findByUser userEntity

        // assertion
        Assert.assertTrue userFollowingEntityFound instanceof UserFollowing

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
        SessionFactory sessionFactoryMock = SessionFactoryFixtures
            .createSessionFactoryMockWithEmptyCriteriaListAndMethods UserFollowing.class
        setSessionFactoryToDao propagationsUserFollowingDAOImpl, sessionFactoryMock

        // exercise
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findOldestPropagationInDiscoverPhase()

         // assertion
        Assert.assertNull userFollowingFoundEntity

        // teardown
        setSessionFactoryToDao propagationsUserFollowingDAOImpl, sessionFactory
    }

    @Test
    void create() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        // exercise
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImpl.create(userEntity, "sleep")

        // assertion
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findByUser userEntity
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
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findByUser userEntity
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

    private static void setSessionFactoryToDao(UserFollowingDAO propagationsUserFollowingDAOImpl, SessionFactory sessionFactory) {
        Field field = propagationsUserFollowingDAOImpl.getClass().getDeclaredField "sessionFactory"
        field.setAccessible true
        field.set propagationsUserFollowingDAOImpl, sessionFactory
    }

}
