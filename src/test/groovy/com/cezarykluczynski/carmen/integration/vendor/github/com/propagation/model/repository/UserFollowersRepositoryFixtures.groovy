package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository

import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User

class UserFollowersRepositoryFixtures {

    private UserFollowersRepository userFollowersRepository

    public UserFollowersRepositoryFixtures(UserFollowersRepository userFollowersRepository) {
        this.userFollowersRepository = userFollowersRepository
    }

    public UserFollowers createUserFollowersEntityUsingUserEntity(User userEntity) {
        return createUserFollowersEntityUsingUserEntityAndPhase(userEntity, "discover")
    }

    public UserFollowers createUserFollowersEntityUsingUserEntityAndPhase(User userEntity, String phase) {
        UserFollowers userFollowersEntity = userFollowersRepository.create(
            userEntity,
            phase
        )

        return userFollowersEntity
    }

    public void deleteUserFollowersEntity(UserFollowers userFollowersEntity) {
        userFollowersRepository.delete userFollowersEntity
    }

    public void deleteUserFollowersEntityByUserEntity(User userEntity) {
        UserFollowers userFollowersEntity = userFollowersRepository.findOneByUser userEntity
        if (userFollowersEntity != null) {
            deleteUserFollowersEntity userFollowersEntity
        }
    }

}
