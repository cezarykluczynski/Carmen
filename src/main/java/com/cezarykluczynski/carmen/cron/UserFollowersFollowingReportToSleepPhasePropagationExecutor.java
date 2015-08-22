package com.cezarykluczynski.carmen.cron;

import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl;
import com.cezarykluczynski.carmen.propagation.UserFollowers;
import com.cezarykluczynski.carmen.propagation.UserFollowing;

import org.springframework.beans.factory.annotation.Autowired;

public class UserFollowersFollowingReportToSleepPhasePropagationExecutor {

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDao;

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDao;

    @Autowired
    UserFollowers propagationUserFollowers;

    @Autowired
    UserFollowing propagationUserFollowing;

    public void run() {
        //
    }

}
