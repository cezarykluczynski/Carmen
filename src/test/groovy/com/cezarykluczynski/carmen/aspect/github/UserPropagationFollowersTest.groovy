package com.cezarykluczynski.carmen.aspect.github

import com.cezarykluczynski.carmen.AspectIntegrationTest
import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.propagation.github.UserFollowersPropagation
import org.springframework.beans.factory.annotation.Autowired

class UserPropagationFollowersTest extends AspectIntegrationTest {

    @Autowired
    private UserDAO githubUserDAOImpl

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private UserPropagationFollowers userPropagationFollowers

    private UserFollowersPropagation userFollowersPropagation

    private User userEntity

    def setup() {
        userFollowersPropagation = Mock UserFollowersPropagation
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userPropagationFollowers.userFollowersPropagation = userFollowersPropagation
    }

    def cleanup() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def followersPropagateAfterUserCreation() {
        when:
        githubUserDAOImpl.createOrUpdateRequestedEntity userEntity.getLogin()

        then:
        1 * userFollowersPropagation.propagate()
    }

}
