package com.cezarykluczynski.carmen.dao.propagations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl

import java.util.List
import java.util.Iterator

@Component
class UserFollowingDAOImplFixtures {

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDao

    public UserFollowing createUserFollowingEntityUsingUserEntity(User userEntity) {
        return createUserFollowingEntityUsingUserEntityAndPhase(userEntity, "discover")
    }

    public UserFollowing createUserFollowingEntityUsingUserEntityAndPhase(User userEntity, String phase) {
        UserFollowing userFollowingEntity = propagationsUserFollowingDao.create(
            userEntity,
            phase
        )

        return userFollowingEntity
    }

    public void deleteUserFollowingEntity(UserFollowing userFollowingEntity) {
        propagationsUserFollowingDao.delete userFollowingEntity
    }

    public void deleteUserFollowingEntityByUserEntity(User userEntity) {
        List<UserFollowing> userFollowingEntitiesList = propagationsUserFollowingDao.findByUser userEntity
        Iterator<UserFollowing> userFollowingEntitiesListIterator = userFollowingEntitiesList.iterator()

        while (userFollowingEntitiesListIterator.hasNext()) {
            deleteUserFollowingEntity userFollowingEntitiesListIterator.next()
        }
    }

}
