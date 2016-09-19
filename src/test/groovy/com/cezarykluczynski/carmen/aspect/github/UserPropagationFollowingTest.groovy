package com.cezarykluczynski.carmen.aspect.github

import com.cezarykluczynski.carmen.AspectIntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import com.cezarykluczynski.carmen.propagation.github.UserFollowingPropagation
import org.springframework.beans.factory.annotation.Autowired

class UserPropagationFollowingTest extends AspectIntegrationTest {

    @Autowired
    private UserRepository userRepository

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserPropagationFollowing userPropagationFollowing

    private UserFollowingPropagation userFollowingPropagation

    private User userEntity

    def setup() {
        userFollowingPropagation = Mock UserFollowingPropagation
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        userPropagationFollowing.userFollowingPropagation = userFollowingPropagation
    }

    def cleanup() {
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def followingPropagateAfterUserCreation() {
        when:
        userRepository.createOrUpdateRequestedEntity userEntity.getLogin()

        then:
        1 * userFollowingPropagation.propagate()
    }

}
