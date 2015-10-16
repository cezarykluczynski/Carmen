package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl;
import com.cezarykluczynski.carmen.propagation.github.UserFollowersFollowingReportToSleepPhasePropagation;
import com.cezarykluczynski.carmen.model.github.User;

import org.springframework.beans.factory.annotation.Autowired;

public class UserFollowersFollowingReportToSleepPhasePropagationExecutor {

    @Autowired
    UserDAOImpl githubUserDAOImpl;

    @Autowired
    UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase;

    public void run() {
        try {
            User userEntity = githubUserDAOImpl.findUserInReportFollowersFolloweesPhase();

            if (userEntity != null) {
                propagationUserFollowersFollowingReportToSleepPhase.setUserEntity(userEntity);
                propagationUserFollowersFollowingReportToSleepPhase.propagate();
            }
        } catch (Exception e) {}
    }

}
