package com.cezarykluczynski.carmen.dao.apiqueue

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.model.propagations.UserFollowing

class PendingRequestDAOImplFixtures {

    private PendingRequestDAO apiqueuePendingRequestDAOImpl

    public PendingRequestDAOImplFixtures(PendingRequestDAO apiqueuePendingRequestDAOImpl) {
        this.apiqueuePendingRequestDAOImpl = apiqueuePendingRequestDAOImpl
    }

    public PendingRequest createPendingRequestEntityUsingUserEntityAndPriority(User userEntity, Integer priority) {
        HashMap<String, Object> emptyParams = new HashMap<String, Object>()
        apiqueuePendingRequestDAOImpl.create(
            "RandomExecutor",
            userEntity,
            emptyParams,
            emptyParams,
            emptyParams,
            priority
        )
    }

    public PendingRequest createPendingRequestEntityUsingUserEntity(User userEntity) {
        createPendingRequestEntityUsingUserEntityAndPriority userEntity, 0
    }

    public PendingRequest createPendingRequestEntityUsingUserEntityAndUserFollowersEntity(User userEntity, UserFollowers userFollowersEntity) {
        HashMap<String, Object> emptyParams = new HashMap<String, Object>()
        PendingRequest pendingRequestEntity = apiqueuePendingRequestDAOImpl.create(
            "RandomExecutor",
            userEntity,
            emptyParams,
            emptyParams,
            emptyParams,
            userFollowersEntity,
            0
        )

        return pendingRequestEntity
    }

    public PendingRequest createPendingRequestEntityUsingUserEntityAndUserFollowingEntity(User userEntity, UserFollowing userFollowingEntity) {
        HashMap<String, Object> emptyParams = new HashMap<String, Object>()
        PendingRequest pendingRequestEntity = apiqueuePendingRequestDAOImpl.create(
            "RandomExecutor",
            userEntity,
            emptyParams,
            emptyParams,
            emptyParams,
            userFollowingEntity,
            0
        )

        return pendingRequestEntity
    }

    public void deletePendingRequestEntity(PendingRequest pendingRequestEntity) {
        apiqueuePendingRequestDAOImpl.delete pendingRequestEntity
    }

    public void deletePendingRequestEntityByUserEntity(User userEntity) {
        List<PendingRequest> pendingRequestEntitiesList = apiqueuePendingRequestDAOImpl.findByUser userEntity
        Iterator<PendingRequest> pendingRequestEntitiesListIterator = pendingRequestEntitiesList.iterator()

        while (pendingRequestEntitiesListIterator.hasNext()) {
            deletePendingRequestEntity pendingRequestEntitiesListIterator.next()
        }
    }

}
