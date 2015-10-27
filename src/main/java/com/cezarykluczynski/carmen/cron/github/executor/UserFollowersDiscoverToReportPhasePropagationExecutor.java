package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl;
import com.cezarykluczynski.carmen.propagation.github.UserFollowersPropagation;

import org.springframework.beans.factory.annotation.Autowired;

public class UserFollowersDiscoverToReportPhasePropagationExecutor {

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDAOImpl;

    @Autowired
    UserFollowersPropagation userFollowersPropagation;

    public void run() {
        UserFollowers userFollowers = propagationsUserFollowersDAOImpl.findOldestPropagationInDiscoverPhase();

        if (userFollowers != null) {
            userFollowersPropagation.tryToMoveToReportPhase(userFollowers.getId());
        }
    }

}
