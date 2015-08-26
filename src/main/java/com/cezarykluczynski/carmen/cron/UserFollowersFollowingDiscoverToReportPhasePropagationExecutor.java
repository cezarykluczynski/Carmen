package com.cezarykluczynski.carmen.cron;

import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl;
import com.cezarykluczynski.carmen.propagation.UserFollowersPropagation;
import com.cezarykluczynski.carmen.propagation.UserFollowingPropagation;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class UserFollowersFollowingDiscoverToReportPhasePropagationExecutor {

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDao;

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDao;

    @Autowired
    UserFollowersPropagation userFollowersPropagation;

    @Autowired
    UserFollowingPropagation userFollowingPropagation;

    private String[] propagationEntities = {
        "UserFollowers",
        "UserFollowing"
    };

    public void run() {
        Integer propagationIndex = randInt(0, propagationEntities.length - 1);
        String propagationName = propagationEntities[propagationIndex];

        if (propagationName.equals("UserFollowers")) {
            tryToMoveUserFollowersToReportPhase();
        } else if (propagationName.equals("UserFollowing")) {
            tryToMoveUserFollowingToReportPhase();
        }
    }

    public void tryToMoveUserFollowersToReportPhase() {
        com.cezarykluczynski.carmen.model.propagations.UserFollowers userFollowers =
            propagationsUserFollowersDao.findOldestPropagationInDiscoverPhase();
        if (userFollowers != null) {
            userFollowersPropagation.tryToMoveToReportPhase(userFollowers.getId());
        }
    }

    public void tryToMoveUserFollowingToReportPhase() {
        com.cezarykluczynski.carmen.model.propagations.UserFollowing userFollowing =
            propagationsUserFollowingDao.findOldestPropagationInDiscoverPhase();
        if (userFollowing != null) {
            userFollowingPropagation.tryToMoveToReportPhase(userFollowing.getId());
        }
    }

    private Integer randInt(Integer min, Integer max) {
        Random rand = new Random();
        Integer randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
