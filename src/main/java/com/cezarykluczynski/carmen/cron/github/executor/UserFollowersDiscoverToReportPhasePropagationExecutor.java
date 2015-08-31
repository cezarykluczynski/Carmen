package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl;
import com.cezarykluczynski.carmen.propagation.UserFollowersPropagation;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class UserFollowersDiscoverToReportPhasePropagationExecutor {

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDao;

    @Autowired
    UserFollowersPropagation userFollowersPropagation;

    public void run() {
        UserFollowers userFollowers = propagationsUserFollowersDao.findOldestPropagationInDiscoverPhase();
        if (userFollowers != null) {
            userFollowersPropagation.tryToMoveToReportPhase(userFollowers.getId());
        }
    }

}
