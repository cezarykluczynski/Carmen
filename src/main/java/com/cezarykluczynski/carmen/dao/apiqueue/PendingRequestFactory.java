package com.cezarykluczynski.carmen.dao.apiqueue;

import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.Repositories;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing;
import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;

import java.util.HashMap;

@Component
public class PendingRequestFactory {

    private PendingRequestRepository pendingRequestRepository;

    @Autowired
    public PendingRequestFactory(PendingRequestRepository pendingRequestRepository) {
        this.pendingRequestRepository = pendingRequestRepository;
    }

    public PendingRequest createPendingRequestForUserRepositoriesPropagation(Propagation repositoriesEntity) {
        User userEntity = ((Repositories) repositoriesEntity).getUser();
        return createPendingRequestEntity(userEntity, repositoriesEntity, "Repositories", "repositories");
    }

    public PendingRequest createPendingRequestForUserFollowersPropagation(Propagation userFollowersEntity) {
        User userEntity = ((UserFollowers) userFollowersEntity).getUser();
        return createPendingRequestEntity(userEntity, userFollowersEntity, "UsersGhostPaginator", "followers_url");
    }

    public PendingRequest createPendingRequestForUserFollowingPropagation(Propagation userFollowingEntity) {
        User userEntity = ((UserFollowing) userFollowingEntity).getUser();
        return createPendingRequestEntity(userEntity, userFollowingEntity, "UsersGhostPaginator", "following_url");
    }

    private PendingRequest createPendingRequestEntity(User userEntity, Propagation propagationEntity, String executor,
                                                      String endpoint) {
        return pendingRequestRepository.create(
            executor,
            userEntity,
            createPropagationPathParams(userEntity, "following_url"),
            Maps.newHashMap(),
            Maps.newHashMap(),
            propagationEntity,
            1
        );
    }

    private HashMap<String, Object> createPropagationPathParams(User userEntity, String endpoint) {
        HashMap<String, Object> pathParams = Maps.newHashMap();
        pathParams.put("endpoint", endpoint);
        pathParams.put("login", userEntity.getLogin());
        return pathParams;
    }

}
