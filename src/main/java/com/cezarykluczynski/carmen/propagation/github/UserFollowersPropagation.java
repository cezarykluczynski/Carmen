package com.cezarykluczynski.carmen.propagation.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.HashMap;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;

@Component
public class UserFollowersPropagation implements com.cezarykluczynski.carmen.propagation.Propagation {

    @Autowired
    UserDAOImpl githubUserDAOImpl;

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDAOImpl;

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDAOImpl;

    private User userEntity;

    @Override
    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public void propagate() {
        if (!userEntity.getFound()) {
            return;
        }

        List<com.cezarykluczynski.carmen.model.propagations.UserFollowers> userFollowersPropagations =
            propagationsUserFollowersDAOImpl.findByUser(userEntity);

        tryCreateDiscoverPhase(userFollowersPropagations);
    }

    public void tryToMoveToReportPhase(PendingRequest pendingRequest) {
        Long propagationId = pendingRequest.getPropagationId();
        tryToMoveToReportPhase(propagationId);
    }

    public void tryToMoveToReportPhase(Long propagationId) {
        Long count = apiqueuePendingRequestDAOImpl.countByPropagationId(propagationId);

        if (count > 0) {
            return;
        }

        com.cezarykluczynski.carmen.model.propagations.UserFollowers userFollowers =
            propagationsUserFollowersDAOImpl.findById(propagationId);

        if (!userFollowers.getPhase().equals("discover")) {
            return;
        }

        userFollowers.setPhase("report");
        propagationsUserFollowersDAOImpl.update(userFollowers);
    }

    private void tryCreateDiscoverPhase(
        List<com.cezarykluczynski.carmen.model.propagations.UserFollowers> userFollowersPropagations
    ) {
        if (userFollowersPropagations.isEmpty()) {
            createDiscoverPhase(userEntity);
        }
    }

    private void createDiscoverPhase(User userEntity) {
        Propagation propagation = propagationsUserFollowersDAOImpl.create(userEntity, "discover");
        HashMap<String, Object> pathParams = new HashMap<String, Object>();
        pathParams.put("endpoint", "followers_url");
        pathParams.put("login", userEntity.getLogin());
        apiqueuePendingRequestDAOImpl.create(
            "UsersGhostPaginator",
            userEntity,
            pathParams,
            new HashMap<String, Object>(),
            new HashMap<String, Object>(),
            propagation,
            1
        );
    }

}
