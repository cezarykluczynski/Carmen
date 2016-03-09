package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.model.propagations.UserFollowing;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFollowingDiscoverToReportPhasePropagationExecutor implements Runnable {

    private UserFollowingDAO propagationsUserFollowingDAOImpl;

    private PendingRequestDAO apiqueuePendingRequestDAOImpl;

    @Autowired
    public UserFollowingDiscoverToReportPhasePropagationExecutor(UserFollowingDAO propagationsUserFollowingDAOImpl,
                                                                 PendingRequestDAO apiqueuePendingRequestDAOImpl) {
        this.propagationsUserFollowingDAOImpl = propagationsUserFollowingDAOImpl;
        this.apiqueuePendingRequestDAOImpl = apiqueuePendingRequestDAOImpl;
    }

    public void run() {
        UserFollowing userFollowing = propagationsUserFollowingDAOImpl.findOldestPropagationInDiscoverPhase();
        tryToMoveToReportPhase(userFollowing);
    }

    private void tryToMoveToReportPhase(UserFollowing userFollowing) {
        if (userFollowing == null || !userFollowing.getPhase().equals("discover")) {
            return;
        }

        Long propagationId = userFollowing.getId();
        Long count = apiqueuePendingRequestDAOImpl.countByPropagationId(propagationId);

        if (count > 0) {
            return;
        }

        userFollowing.setPhase("report");
        propagationsUserFollowingDAOImpl.update(userFollowing);
    }

}
