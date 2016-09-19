package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import org.springframework.beans.factory.annotation.Autowired

class UserFollowingPropagationTest extends IntegrationTest {

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserFollowingPropagation userFollowingPropagation

    @Autowired
    private UserFollowingRepository userFollowingRepository

    @Autowired
    private UserFollowingRepositoryFixtures userFollowingRepositoryFixtures

    private User userEntity

    void cleanup() {
        userFollowingRepositoryFixtures.deleteUserFollowingEntityByUserEntity userEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "propagates not found entity"() {
        given:
        userEntity = userRepositoryFixtures.createNotFoundRequestedUserEntity()
        userFollowingPropagation.setUserEntity userEntity

        when:
        userFollowingPropagation.propagate()

        then:
        userFollowingRepository.findOneByUser(userEntity) == null
    }

    def "propagates non-empty propagations"() {
        given:
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = userFollowingRepositoryFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowingPropagation.setUserEntity userEntity

        when:
        userFollowingPropagation.propagate()
        UserFollowing propagationsUserFollowingEntity = userFollowingRepository.findOneByUser(userEntity)

        then:
        propagationsUserFollowingEntity instanceof UserFollowing
        propagationsUserFollowingEntity.getId() == userFollowingEntity.getId()
        propagationsUserFollowingEntity.getPhase() == userFollowingEntity.getPhase()

        cleanup:
        userFollowingRepositoryFixtures.deleteUserFollowingEntity userFollowingEntity
    }

    def "propagates found entity"() {
        given:
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        userFollowingPropagation.setUserEntity userEntity

        when:
        userFollowingPropagation.propagate()
        UserFollowing propagationsUserFollowingEntity = userFollowingRepository.findOneByUser(userEntity)

        then:
        propagationsUserFollowingEntity instanceof UserFollowing
        propagationsUserFollowingEntity.getPhase() == "discover"
    }

}
