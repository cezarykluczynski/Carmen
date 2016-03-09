package com.cezarykluczynski.carmen.dao.propagations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowing

@Component
class UserFollowingDAOImplFixtures {

    private UserFollowingDAO propagationsUserFollowingDAOImpl

    @Autowired
    public UserFollowingDAOImplFixtures(UserFollowingDAO propagationsUserFollowingDAOImpl) {
        this.propagationsUserFollowingDAOImpl = propagationsUserFollowingDAOImpl
    }

    public UserFollowing createUserFollowingEntityUsingUserEntity(User userEntity) {
        return createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")
    }

    public UserFollowing createUserFollowingEntityUsingUserEntityAndPhase(User userEntity, String phase) {
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImpl.create(
            userEntity,
            phase
        )

        return userFollowingEntity
    }

    public void deleteUserFollowingEntity(UserFollowing userFollowingEntity) {
        propagationsUserFollowingDAOImpl.delete userFollowingEntity
    }

    public void deleteUserFollowingEntityByUserEntity(User userEntity) {
        UserFollowing userFollowingEntity = propagationsUserFollowingDAOImpl.findByUser userEntity
        if (userFollowingEntity != null) {
            deleteUserFollowingEntity userFollowingEntity
        }
    }

}
