package com.cezarykluczynski.carmen.dao.apiqueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.model.propagations.Repositories;
import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.model.propagations.UserFollowing;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.model.github.User;

import java.util.HashMap;

@Component
public class PendingRequestFactory {

    @Autowired
    PendingRequestDAO apiqueuePendingRequestDAOImpl;

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
        return apiqueuePendingRequestDAOImpl.create(
            executor,
            userEntity,
            createPropagationPathParams(userEntity, "following_url"),
            newHashMap(),
            newHashMap(),
            propagationEntity,
            1
        );
    }

    private HashMap<String, Object> createPropagationPathParams(User userEntity, String endpoint) {
        HashMap<String, Object> pathParams = new HashMap<String, Object>();
        pathParams.put("endpoint", endpoint);
        pathParams.put("login", userEntity.getLogin());
        return pathParams;
    }

    private HashMap<String, Object> newHashMap() {
        return new HashMap<String, Object>();
    }

}
