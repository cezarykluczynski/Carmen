package com.cezarykluczynski.carmen.propagation;

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
    UserFollowersDAOImpl propagationsUserFollowersDao;

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao;

    private User userEntity;

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    public void propagate() {
        if (!userEntity.getFound()) {
            return;
        }

        List<com.cezarykluczynski.carmen.model.propagations.UserFollowers> userFollowersPropagations =
            propagationsUserFollowersDao.findByUser(userEntity);

        tryCreateDiscoverPhase(userFollowersPropagations);
    }

    public void tryToMoveToReportPhase(PendingRequest pendingRequest) {
        Long propagationId = pendingRequest.getPropagationId();
        tryToMoveToReportPhase(propagationId);
    }

    public void tryToMoveToReportPhase(Long propagationId) {
        Long count = apiqueuePendingRequestDao.countByPropagationId(propagationId);

        if (count > 0) {
            return;
        }

        com.cezarykluczynski.carmen.model.propagations.UserFollowers userFollowers =
            propagationsUserFollowersDao.findById(propagationId);

        if (!userFollowers.getPhase().equals("discover")) {
            return;
        }

        userFollowers.setPhase("report");
        propagationsUserFollowersDao.update(userFollowers);
    }

    private void tryCreateDiscoverPhase(
        List<com.cezarykluczynski.carmen.model.propagations.UserFollowers> userFollowersPropagations
    ) {
        if (userFollowersPropagations.isEmpty()) {
            createDiscoverPhase(userEntity);
            return;
        }
    }

    private void createDiscoverPhase(User userEntity) {
        Propagation propagation = propagationsUserFollowersDao.create(userEntity, "discover");
        HashMap<String, Object> pathParams = new HashMap<String, Object>();
        pathParams.put("endpoint", "followers_url");
        pathParams.put("login", userEntity.getLogin());
        apiqueuePendingRequestDao.create(
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
