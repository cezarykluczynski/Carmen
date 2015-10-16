package com.cezarykluczynski.carmen.propagation.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.HashMap;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;

@Component
public class UserFollowingPropagation implements com.cezarykluczynski.carmen.propagation.Propagation {

    @Autowired
    UserDAOImpl githubUserDAOImpl;

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDao;

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao;

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

        List<com.cezarykluczynski.carmen.model.propagations.UserFollowing> userFollowingPropagations =
            propagationsUserFollowingDao.findByUser(userEntity);

        tryCreateDiscoverPhase(userFollowingPropagations);
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

        com.cezarykluczynski.carmen.model.propagations.UserFollowing userFollowing =
            propagationsUserFollowingDao.findById(propagationId);

        if (!userFollowing.getPhase().equals("discover")) {
            return;
        }

        userFollowing.setPhase("report");
        propagationsUserFollowingDao.update(userFollowing);
    }

    private void tryCreateDiscoverPhase(
        List<com.cezarykluczynski.carmen.model.propagations.UserFollowing> userFollowingPropagations
    ) {
        if (userFollowingPropagations.isEmpty()) {
            createDiscoverPhase(userEntity);
        }
    }

    private void createDiscoverPhase(User userEntity) {
        Propagation propagation = propagationsUserFollowingDao.create(userEntity, "discover");
        HashMap<String, Object> pathParams = new HashMap<String, Object>();
        pathParams.put("endpoint", "following_url");
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
