package com.cezarykluczynski.carmen.aspect.github

import com.cezarykluczynski.carmen.AspectIntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import com.cezarykluczynski.carmen.propagation.github.UserFollowersPropagation
import org.springframework.beans.factory.annotation.Autowired

class UserPropagationFollowersTest extends AspectIntegrationTest {

    @Autowired
    private UserRepository userRepository

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserPropagationFollowers userPropagationFollowers

    private UserFollowersPropagation userFollowersPropagation

    private User userEntity

    def setup() {
        userFollowersPropagation = Mock UserFollowersPropagation
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        userPropagationFollowers.userFollowersPropagation = userFollowersPropagation
    }

    def cleanup() {
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def followersPropagateAfterUserCreation() {
        when:
        userRepository.createOrUpdateRequestedEntity userEntity.getLogin()

        then:
        1 * userFollowersPropagation.propagate()
    }

}
