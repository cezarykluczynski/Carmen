package com.cezarykluczynski.carmen.dao.propagations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO

import java.util.List
import java.util.Iterator

@Component
class UserFollowersDAOImplFixtures {

    @Autowired
    UserFollowersDAO propagationsUserFollowersDAOImpl

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
