package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import org.joda.time.DateTimeConstants
import org.springframework.beans.factory.annotation.Autowired

class UserFollowersRepositoryTest extends IntegrationTest {

    private static final String DISCOVER_PHASE = "discover"
    private static final String SLEEP_PHASE = "sleep"

    @Autowired
    private UserFollowersRepository userFollowersRepository

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserFollowersRepositoryFixtures userFollowersRepositoryFixtures

    def "entity is found by user"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = userFollowersRepositoryFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, DISCOVER_PHASE)

        when:
        UserFollowers userFollowersFoundEntity = userFollowersRepository.findOneByUser userEntity

        then:
        userFollowersFoundEntity instanceof UserFollowers

        cleanup:
        userFollowersRepositoryFixtures.deleteUserFollowersEntity userFollowersEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "oldest propagation in discover phase is found when it exists"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = userFollowersRepositoryFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, DISCOVER_PHASE)

        userFollowersEntity.updated = new Date(DateTimeConstants.SECONDS_PER_DAY)
        userFollowersRepository.save userFollowersEntity

        when:
        UserFollowers userFollowersFoundEntity = userFollowersRepository.findOldestPropagationInDiscoverPhase()

        then:
        userFollowersFoundEntity.id == userFollowersEntity.id

        cleanup:
        userFollowersRepositoryFixtures.deleteUserFollowersEntity userFollowersEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "entity is created using user entity and phase name"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()

        when:
        UserFollowers userFollowersEntity = userFollowersRepository.create(userEntity, SLEEP_PHASE)
        UserFollowers userFollowersFoundEntity = userFollowersRepository.findOneByUser userEntity

        then:
        userFollowersFoundEntity.id == userFollowersEntity.id
        userFollowersFoundEntity.phase == SLEEP_PHASE

        cleanup:
        userFollowersRepository.delete userFollowersEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "entity is updated"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = userFollowersRepository.create(userEntity, SLEEP_PHASE)
        userFollowersEntity.setPhase DISCOVER_PHASE

        when:
        userFollowersRepository.save userFollowersEntity
        UserFollowers userFollowersFoundEntity = userFollowersRepository.findOneByUser userEntity

        then:
        userFollowersFoundEntity.phase == DISCOVER_PHASE

        cleanup:
        userFollowersRepository.delete userFollowersEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "entity is deleted"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = userFollowersRepository.create(userEntity, SLEEP_PHASE)

        when:
        userFollowersRepository.delete userFollowersEntity

        then:
        userFollowersRepository.findOne(userFollowersEntity.getId()) == null

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "existing entity is found by id"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = userFollowersRepository.create(userEntity, SLEEP_PHASE)

        when:
        UserFollowers userFollowersFoundEntity = userFollowersRepository.findOne(userFollowersEntity.getId())

        then:
        userFollowersFoundEntity instanceof UserFollowers

        cleanup:
        userFollowersRepository.delete userFollowersEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "non existing entity is not found"() {
        when:
        UserFollowers userFollowersFoundEntity = userFollowersRepository.findOne 2147483647L

        then:
        userFollowersFoundEntity == null
    }

}
