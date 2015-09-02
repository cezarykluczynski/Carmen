package com.cezarykluczynski.carmen.dao.propagations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl

import java.util.HashMap

@Component
class UserFollowersDAOImplFixtures {

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDao

    public UserFollowers createUserFollowersEntityUsingUserEntity(User userEntity) {
        UserFollowers userFollowersEntity = propagationsUserFollowersDao.create(
            userEntity,
            "discover"
        )

        return userFollowersEntity
    }

    public void deleteUserFollowersEntity(UserFollowers userFollowersEntity) {
        propagationsUserFollowersDao.delete userFollowersEntity
    }

}
