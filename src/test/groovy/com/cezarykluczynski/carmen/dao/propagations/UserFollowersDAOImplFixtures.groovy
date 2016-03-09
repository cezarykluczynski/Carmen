package com.cezarykluczynski.carmen.dao.propagations

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowers

class UserFollowersDAOImplFixtures {

    private UserFollowersDAO propagationsUserFollowersDAOImpl

    public UserFollowersDAOImplFixtures(UserFollowersDAO propagationsUserFollowersDAOImpl) {
        this.propagationsUserFollowersDAOImpl = propagationsUserFollowersDAOImpl
    }

    public UserFollowers createUserFollowersEntityUsingUserEntity(User userEntity) {
        return createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")
    }

    public UserFollowers createUserFollowersEntityUsingUserEntityAndPhase(User userEntity, String phase) {
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImpl.create(
            userEntity,
            phase
        )

        return userFollowersEntity
    }

    public void deleteUserFollowersEntity(UserFollowers userFollowersEntity) {
        propagationsUserFollowersDAOImpl.delete userFollowersEntity
    }

    public void deleteUserFollowersEntityByUserEntity(User userEntity) {
        UserFollowers userFollowersEntity = propagationsUserFollowersDAOImpl.findByUser userEntity
        if (userFollowersEntity != null) {
            deleteUserFollowersEntity userFollowersEntity
        }
    }

}
