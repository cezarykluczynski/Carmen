package com.cezarykluczynski.carmen.dao.propagations

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.fixture.org.hibernate.SessionFactoryFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import org.hibernate.SessionFactory
import org.joda.time.DateTimeConstants
import org.springframework.beans.factory.annotation.Autowired
import org.testng.Assert

class UserFollowersDAOImplTest extends IntegrationTest {

    private static final String DISCOVER_PHASE = "discover"
    private static final String SLEEP_PHASE = "sleep"

    private UserFollowersDAOImpl propagationsUserFollowersDAOImpl

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    private SessionFactory sessionFactory

    def setup() {
        propagationsUserFollowersDAOImpl = new UserFollowersDAOImpl(sessionFactory)
    }

    def "entity is found by user"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, DISCOVER_PHASE)

        when:
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findByUser userEntity

        then:
        Assert.assertTrue userFollowersFoundEntity instanceof UserFollowers

        cleanup:
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "oldest propagation in discover phase is found when it exists"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, DISCOVER_PHASE)

        userFollowersEntity.updated = new Date(DateTimeConstants.SECONDS_PER_DAY)
        propagationsUserFollowersDAOImpl.update userFollowersEntity

        when:
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findOldestPropagationInDiscoverPhase()

        then:
        userFollowersFoundEntity.id == userFollowersEntity.id

        cleanup:
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "oldest propagation in discover phase is not found when it does not exists"() {
        given:
        SessionFactory sessionFactoryMock = SessionFactoryFixtures
            .createSessionFactoryMockWithEmptyCriteriaListAndMethods UserFollowers.class
        setSessionFactoryToDao propagationsUserFollowersDAOImpl, sessionFactoryMock

        when:
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findOldestPropagationInDiscoverPhase()

        then:
        userFollowersFoundEntity == null

        cleanup:
        setSessionFactoryToDao propagationsUserFollowersDAOImpl, sessionFactory
    }

    def "entity is created using user entity and phase name"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        when:
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImpl.create(userEntity, SLEEP_PHASE)
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findByUser userEntity

        then:
        userFollowersFoundEntity.id == userFollowersEntity.id
        userFollowersFoundEntity.phase == SLEEP_PHASE

        cleanup:
        propagationsUserFollowersDAOImpl.delete userFollowersEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    void update() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImpl.create(userEntity, SLEEP_PHASE)
        userFollowersEntity.setPhase DISCOVER_PHASE

        // exercise
        propagationsUserFollowersDAOImpl.update userFollowersEntity

        // assertion
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findByUser userEntity
        Assert.assertEquals userFollowersFoundEntity.getPhase(), DISCOVER_PHASE

        // teardown
        propagationsUserFollowersDAOImpl.delete userFollowersEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "entity is deleted"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImpl.create(userEntity, SLEEP_PHASE)

        when:
        propagationsUserFollowersDAOImpl.delete userFollowersEntity

        then:
        propagationsUserFollowersDAOImpl.findById(userFollowersEntity.getId()) == null

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "existing entity is found by id"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImpl.create(userEntity, SLEEP_PHASE)

        when:
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findById(userFollowersEntity.getId())

        then:
        userFollowersFoundEntity instanceof UserFollowers

        cleanup:
        propagationsUserFollowersDAOImpl.delete userFollowersEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "non existing entity is not found"() {
        when:
        UserFollowers userFollowersFoundEntity = propagationsUserFollowersDAOImpl.findById 2147483647

        then:
        userFollowersFoundEntity == null
    }

    private static void setSessionFactoryToDao(UserFollowersDAOImpl propagationsUserFollowersDAOImpl, SessionFactory sessionFactory) {
        propagationsUserFollowersDAOImpl.sessionFactory = sessionFactory
    }

}
