package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers
import org.springframework.beans.factory.annotation.Autowired

class UserFollowersPropagationTest extends IntegrationTest {

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserFollowersPropagation userFollowersPropagation

    @Autowired
    private UserFollowersRepository userFollowersRepository

    @Autowired
    private UserFollowersRepositoryFixtures userFollowersRepositoryFixtures

    private User userEntity

    def cleanup() {
        userFollowersRepositoryFixtures.deleteUserFollowersEntityByUserEntity userEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "propagates not found entity"() {
        given:
        userEntity = userRepositoryFixtures.createNotFoundRequestedUserEntity()
        userFollowersPropagation.setUserEntity userEntity

        when:
        userFollowersPropagation.propagate()

        then:
        userFollowersRepository.findOneByUser(userEntity) == null
    }

    def "propagates non-empty propagations"() {
        given:
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = userFollowersRepositoryFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowersPropagation.setUserEntity userEntity

        when:
        userFollowersPropagation.propagate()
        UserFollowers propagationsUserFollowersEntity = userFollowersRepository.findOneByUser(userEntity)

        then:
        propagationsUserFollowersEntity instanceof UserFollowers
        propagationsUserFollowersEntity.getId() == userFollowersEntity.getId()
        propagationsUserFollowersEntity.getPhase() == userFollowersEntity.getPhase()

        cleanup:
        userFollowersRepositoryFixtures.deleteUserFollowersEntity userFollowersEntity
    }

    def "propagates found entity"() {
        given:
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        userFollowersPropagation.setUserEntity userEntity

        when:
        userFollowersPropagation.propagate()
        UserFollowers propagationsUserFollowersEntity = userFollowersRepository.findOneByUser(userEntity)

        then:
        propagationsUserFollowersEntity instanceof UserFollowers
        propagationsUserFollowersEntity.getPhase() == "discover"
    }

}
