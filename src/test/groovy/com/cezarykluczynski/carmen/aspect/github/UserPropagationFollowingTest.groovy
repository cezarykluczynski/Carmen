package com.cezarykluczynski.carmen.aspect.github

import com.cezarykluczynski.carmen.AspectIntegrationTest
import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.propagation.github.UserFollowingPropagation
import org.springframework.beans.factory.annotation.Autowired

class UserPropagationFollowingTest extends AspectIntegrationTest {

    @Autowired
    private UserDAO githubUserDAOImpl

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private UserPropagationFollowing userPropagationFollowing

    private UserFollowingPropagation userFollowingPropagation

    private User userEntity

    def setup() {
        userFollowingPropagation = Mock UserFollowingPropagation
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userPropagationFollowing.userFollowingPropagation = userFollowingPropagation
    }

    def cleanup() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def followingPropagateAfterUserCreation() {
        when:
        githubUserDAOImpl.createOrUpdateRequestedEntity userEntity.getLogin()

        then:
        1 * userFollowingPropagation.propagate()
    }

}
