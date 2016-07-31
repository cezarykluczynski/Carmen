package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import org.springframework.beans.factory.annotation.Autowired

class UserFollowersPropagationTest extends IntegrationTest {

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private UserFollowersPropagation userFollowersPropagation

    @Autowired
    private UserFollowersDAO propagationsUserFollowersDAOImpl

    @Autowired
    private UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    private User userEntity

    def cleanup() {
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntityByUserEntity userEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "propagates not found entity"() {
        given:
        userEntity = githubUserDAOImplFixtures.createNotFoundRequestedUserEntity()
        userFollowersPropagation.setUserEntity userEntity

        when:
        userFollowersPropagation.propagate()

        then:
        propagationsUserFollowersDAOImpl.findByUser(userEntity) == null
    }

    def "propagates non-empty propagations"() {
        given:
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowersPropagation.setUserEntity userEntity

        when:
        userFollowersPropagation.propagate()
        UserFollowers propagationsUserFollowersEntity = propagationsUserFollowersDAOImpl.findByUser(userEntity)

        then:
        propagationsUserFollowersEntity instanceof UserFollowers
        propagationsUserFollowersEntity.getId() == userFollowersEntity.getId()
        propagationsUserFollowersEntity.getPhase() == userFollowersEntity.getPhase()

        cleanup:
        propagationsUserFollowersDAOImplFixtures.deleteUserFollowersEntity userFollowersEntity
    }

    def "propagates found entity"() {
        given:
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userFollowersPropagation.setUserEntity userEntity

        when:
        userFollowersPropagation.propagate()
        UserFollowers propagationsUserFollowersEntity = propagationsUserFollowersDAOImpl.findByUser(userEntity)

        then:
        propagationsUserFollowersEntity instanceof UserFollowers
        propagationsUserFollowersEntity.getPhase() == "discover"
    }

}
