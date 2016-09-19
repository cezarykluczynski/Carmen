package com.cezarykluczynski.carmen.aspect.github

import com.cezarykluczynski.carmen.AspectIntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import com.cezarykluczynski.carmen.propagation.github.UserRepositoriesPropagation
import org.springframework.beans.factory.annotation.Autowired

class UserPropagationRepositoriesTest extends AspectIntegrationTest {

    @Autowired
    private UserRepository userRepository

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserPropagationRepositories userPropagationRepositories

    private UserRepositoriesPropagation userRepositoriesPropagation

    private User userEntity

    def setup() {
        userRepositoriesPropagation = Mock UserRepositoriesPropagation
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        userPropagationRepositories.userRepositoriesPropagation = userRepositoriesPropagation
    }

    def cleanup() {
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def repositoriesPropagateAfterUserCreation() {
        when:
        userRepository.createOrUpdateRequestedEntity userEntity.getLogin()

        then:
        1 * userRepositoriesPropagation.propagate()
    }

}
