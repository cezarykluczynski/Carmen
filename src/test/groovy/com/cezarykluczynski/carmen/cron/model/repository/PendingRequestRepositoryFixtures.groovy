package com.cezarykluczynski.carmen.cron.model.repository

import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User

class PendingRequestRepositoryFixtures {

    private PendingRequestRepository pendingRequestRepository

    public PendingRequestRepositoryFixtures(PendingRequestRepository pendingRequestRepository) {
        this.pendingRequestRepository = pendingRequestRepository
    }

    public PendingRequest createPendingRequestEntityUsingUserEntityAndPriority(User userEntity, Integer priority) {
        HashMap<String, Object> emptyParams = new HashMap<String, Object>()
        pendingRequestRepository.create(
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
        PendingRequest pendingRequestEntity = pendingRequestRepository.create(
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
        PendingRequest pendingRequestEntity = pendingRequestRepository.create(
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
        pendingRequestRepository.delete pendingRequestEntity
    }

    public void deletePendingRequestEntityByUserEntity(User userEntity) {
        List<PendingRequest> pendingRequestEntitiesList = pendingRequestRepository.findByUser userEntity
        Iterator<PendingRequest> pendingRequestEntitiesListIterator = pendingRequestEntitiesList.iterator()

        while (pendingRequestEntitiesListIterator.hasNext()) {
            deletePendingRequestEntity pendingRequestEntitiesListIterator.next()
        }
    }

}
