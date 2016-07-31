package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
import org.springframework.beans.factory.annotation.Autowired

class UserFollowingPropagationTest extends IntegrationTest {

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private UserFollowingPropagation userFollowingPropagation

    @Autowired
    private UserFollowingDAO propagationsUserFollowingDAOImpl

    @Autowired
    private UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    private User userEntity

    void cleanup() {
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntityByUserEntity userEntity
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "propagates not found entity"() {
        given:
        userEntity = githubUserDAOImplFixtures.createNotFoundRequestedUserEntity()
        userFollowingPropagation.setUserEntity userEntity

        when:
        userFollowingPropagation.propagate()

        then:
        propagationsUserFollowingDAOImpl.findByUser(userEntity) == null
    }

    def "propagates non-empty propagations"() {
        given:
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")
        userFollowingPropagation.setUserEntity userEntity

        when:
        userFollowingPropagation.propagate()
        UserFollowing propagationsUserFollowingEntity = propagationsUserFollowingDAOImpl.findByUser(userEntity)

        then:
        propagationsUserFollowingEntity instanceof UserFollowing
        propagationsUserFollowingEntity.getId() == userFollowingEntity.getId()
        propagationsUserFollowingEntity.getPhase() == userFollowingEntity.getPhase()

        cleanup:
        propagationsUserFollowingDAOImplFixtures.deleteUserFollowingEntity userFollowingEntity
    }

    def "propagates found entity"() {
        given:
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userFollowingPropagation.setUserEntity userEntity

        when:
        userFollowingPropagation.propagate()
        UserFollowing propagationsUserFollowingEntity = propagationsUserFollowingDAOImpl.findByUser(userEntity)

        then:
        propagationsUserFollowingEntity instanceof UserFollowing
        propagationsUserFollowingEntity.getPhase() == "discover"
    }

}
