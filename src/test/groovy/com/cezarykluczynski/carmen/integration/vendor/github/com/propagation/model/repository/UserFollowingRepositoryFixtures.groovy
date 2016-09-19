package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository

import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User

class UserFollowingRepositoryFixtures {

    private UserFollowingRepository userFollowingRepository

    public UserFollowingRepositoryFixtures(UserFollowingRepository userFollowingRepository) {
        this.userFollowingRepository = userFollowingRepository
    }

    public UserFollowing createUserFollowingEntityUsingUserEntityAndPhase(User userEntity, String phase) {
        UserFollowing userFollowingEntity = userFollowingRepository.create(userEntity, phase)

        return userFollowingEntity
    }

    public void deleteUserFollowingEntity(UserFollowing userFollowingEntity) {
        userFollowingRepository.delete userFollowingEntity
    }

    public void deleteUserFollowingEntityByUserEntity(User userEntity) {
        UserFollowing userFollowingEntity = userFollowingRepository.findOneByUser userEntity
        if (userFollowingEntity != null) {
            deleteUserFollowingEntity userFollowingEntity
        }
    }

}
