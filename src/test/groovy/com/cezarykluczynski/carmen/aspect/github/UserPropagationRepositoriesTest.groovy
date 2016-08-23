package com.cezarykluczynski.carmen.aspect.github

import com.cezarykluczynski.carmen.AspectIntegrationTest
import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.propagation.github.UserRepositoriesPropagation
import org.springframework.beans.factory.annotation.Autowired

class UserPropagationRepositoriesTest extends AspectIntegrationTest {

    @Autowired
    private UserDAO githubUserDAOImpl

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private UserPropagationRepositories userPropagationRepositories

    private UserRepositoriesPropagation userRepositoriesPropagation

    private User userEntity

    def setup() {
        userRepositoriesPropagation = Mock UserRepositoriesPropagation
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userPropagationRepositories.userRepositoriesPropagation = userRepositoriesPropagation
    }

    def cleanup() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def repositoriesPropagateAfterUserCreation() {
        when:
        githubUserDAOImpl.createOrUpdateRequestedEntity userEntity.getLogin()

        then:
        1 * userRepositoriesPropagation.propagate()
    }

}
