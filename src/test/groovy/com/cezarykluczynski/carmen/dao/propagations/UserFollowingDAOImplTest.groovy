package com.cezarykluczynski.carmen.dao.propagations

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
import org.hibernate.SessionFactory
import org.joda.time.DateTimeConstants
import org.springframework.beans.factory.annotation.Autowired

class UserFollowingDAOImplTest extends IntegrationTest {

    private static final String DISCOVER_PHASE = "discover"
    private static final String SLEEP_PHASE = "sleep"

    private UserFollowingDAOImpl propagationsUserFollowingDAOImpl

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    @Autowired
    private SessionFactory sessionFactory

    def setup() {
        propagationsUserFollowingDAOImpl = new UserFollowingDAOImpl(sessionFactory)
    }

    def "entity is found by user"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
                .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, DISCOVER_PHASE)

        when:
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findByUser userEntity

        then:
        userFollowingFoundEntity instanceof UserFollowing

        cleanup:
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "oldest propagation in discover phase is found when it exists"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
                .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, DISCOVER_PHASE)

        userFollowingEntity.updated = new Date(DateTimeConstants.SECONDS_PER_DAY)
        propagationsUserFollowingDAOImpl.update userFollowingEntity

        when:
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findOldestPropagationInDiscoverPhase()

        then:
        userFollowingFoundEntity.id == userFollowingEntity.id

        cleanup:
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "oldest propagation in discover phase is not found when it does not exists"() {
        given:
        SessionFactory sessionFactoryMock = createSessionFactoryMockWithEmptyCriteriaListAndMethods UserFollowing.class
        setSessionFactoryToDao propagationsUserFollowingDAOImpl, sessionFactoryMock

        when:
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findOldestPropagationInDiscoverPhase()

        then:
        userFollowingFoundEntity == null

        cleanup:
        setSessionFactoryToDao propagationsUserFollowingDAOImpl, sessionFactory
    }

    def "entity is created using user entity and phase name"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        when:
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImpl.create(userEntity, SLEEP_PHASE)
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findByUser userEntity

        then:
        userFollowingFoundEntity.id == userFollowingEntity.id
        userFollowingFoundEntity.phase == SLEEP_PHASE

        cleanup:
        propagationsUserFollowingDAOImpl.delete userFollowingEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "entity is updated"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImpl.create(userEntity, SLEEP_PHASE)
        userFollowingEntity.setPhase DISCOVER_PHASE

        when:
        propagationsUserFollowingDAOImpl.update userFollowingEntity
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findByUser userEntity

        then:
        userFollowingFoundEntity.phase == DISCOVER_PHASE

        cleanup:
        propagationsUserFollowingDAOImpl.delete userFollowingEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "entity is deleted"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImpl.create(userEntity, SLEEP_PHASE)

        when:
        propagationsUserFollowingDAOImpl.delete userFollowingEntity

        then:
        propagationsUserFollowingDAOImpl.findById(userFollowingEntity.getId()) == null

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "existing entity is found by id"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImpl.create(userEntity, SLEEP_PHASE)

        when:
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findById(userFollowingEntity.getId())

        then:
        userFollowingFoundEntity instanceof UserFollowing

        cleanup:
        propagationsUserFollowingDAOImpl.delete userFollowingEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "non existing entity is not found"() {
        when:
        UserFollowing userFollowingFoundEntity = propagationsUserFollowingDAOImpl.findById 2147483647

        then:
        userFollowingFoundEntity == null
    }

    private static void setSessionFactoryToDao(UserFollowingDAOImpl propagationsUserFollowingDAOImpl, SessionFactory sessionFactory) {
        propagationsUserFollowingDAOImpl.sessionFactory = sessionFactory
    }

}
