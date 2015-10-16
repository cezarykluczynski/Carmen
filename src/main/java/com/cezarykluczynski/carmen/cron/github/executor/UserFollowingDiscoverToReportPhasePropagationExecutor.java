package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.model.propagations.UserFollowing;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl;
import com.cezarykluczynski.carmen.propagation.github.UserFollowingPropagation;

import org.springframework.beans.factory.annotation.Autowired;

public class UserFollowingDiscoverToReportPhasePropagationExecutor {

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDao;

    @Autowired
    UserFollowingPropagation userFollowingPropagation;

    public void run() {
        UserFollowing userFollowing = propagationsUserFollowingDao.findOldestPropagationInDiscoverPhase();

        if (userFollowing != null) {
            userFollowingPropagation.tryToMoveToReportPhase(userFollowing.getId());
        }
    }

}
