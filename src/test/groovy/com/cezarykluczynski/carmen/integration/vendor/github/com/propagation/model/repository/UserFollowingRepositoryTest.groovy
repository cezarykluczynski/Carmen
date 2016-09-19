package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import org.joda.time.DateTimeConstants
import org.springframework.beans.factory.annotation.Autowired

class UserFollowingRepositoryTest extends IntegrationTest {

    private static final String DISCOVER_PHASE = "discover"
    private static final String SLEEP_PHASE = "sleep"

    @Autowired
    private UserFollowingRepository userFollowingRepository

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserFollowingRepositoryFixtures userFollowingRepositoryFixtures

    def "entity is found by user"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = userFollowingRepositoryFixtures
                .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, DISCOVER_PHASE)

        when:
        UserFollowing userFollowingFoundEntity = userFollowingRepository.findOneByUser userEntity

        then:
        userFollowingFoundEntity instanceof UserFollowing

        cleanup:
        userFollowingRepositoryFixtures.deleteUserFollowingEntity userFollowingEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "oldest propagation in discover phase is found when it exists"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = userFollowingRepositoryFixtures
                .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, DISCOVER_PHASE)

        userFollowingEntity.updated = new Date(DateTimeConstants.SECONDS_PER_DAY)
        userFollowingRepository.save userFollowingEntity

        when:
        UserFollowing userFollowingFoundEntity = userFollowingRepository.findOldestPropagationInDiscoverPhase()

        then:
        userFollowingFoundEntity.id == userFollowingEntity.id

        cleanup:
        userFollowingRepositoryFixtures.deleteUserFollowingEntity userFollowingEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "entity is created using user entity and phase name"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()

        when:
        UserFollowing userFollowingEntity = userFollowingRepository.create(userEntity, SLEEP_PHASE)
        UserFollowing userFollowingFoundEntity = userFollowingRepository.findOneByUser userEntity

        then:
        userFollowingFoundEntity.id == userFollowingEntity.id
        userFollowingFoundEntity.phase == SLEEP_PHASE

        cleanup:
        userFollowingRepository.delete userFollowingEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "entity is updated"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = userFollowingRepository.create(userEntity, SLEEP_PHASE)
        userFollowingEntity.setPhase DISCOVER_PHASE

        when:
        userFollowingRepository.save userFollowingEntity
        UserFollowing userFollowingFoundEntity = userFollowingRepository.findOneByUser userEntity

        then:
        userFollowingFoundEntity.phase == DISCOVER_PHASE

        cleanup:
        userFollowingRepository.delete userFollowingEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "entity is deleted"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = userFollowingRepository.create(userEntity, SLEEP_PHASE)

        when:
        userFollowingRepository.delete userFollowingEntity

        then:
        userFollowingRepository.findOne(userFollowingEntity.getId()) == null

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "existing entity is found by id"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = userFollowingRepository.create(userEntity, SLEEP_PHASE)

        when:
        UserFollowing userFollowingFoundEntity = userFollowingRepository.findOne(userFollowingEntity.getId())

        then:
        userFollowingFoundEntity instanceof UserFollowing

        cleanup:
        userFollowingRepository.delete userFollowingEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "non existing entity is not found"() {
        when:
        UserFollowing userFollowingFoundEntity = userFollowingRepository.findOne 2147483647L

        then:
        userFollowingFoundEntity == null
    }

}
