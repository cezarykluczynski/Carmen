package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO;

import org.springframework.beans.factory.annotation.Autowired;

public class UserFollowersDiscoverToReportPhasePropagationExecutor {

    @Autowired
    UserFollowersDAO propagationsUserFollowersDAOImpl;

    @Autowired
    PendingRequestDAO apiqueuePendingRequestDAOImpl;

    public void run() {
        UserFollowers userFollowers = propagationsUserFollowersDAOImpl.findOldestPropagationInDiscoverPhase();
        tryToMoveToReportPhase(userFollowers);
    }

    private void tryToMoveToReportPhase(UserFollowers userFollowers) {
        if (userFollowers == null || !userFollowers.getPhase().equals("discover")) {
            return;
        }

        Long propagationId = userFollowers.getId();
        Long count = apiqueuePendingRequestDAOImpl.countByPropagationId(propagationId);

        if (count > 0) {
            return;
        }

        userFollowers.setPhase("report");
        propagationsUserFollowersDAOImpl.update(userFollowers);
    }

}
