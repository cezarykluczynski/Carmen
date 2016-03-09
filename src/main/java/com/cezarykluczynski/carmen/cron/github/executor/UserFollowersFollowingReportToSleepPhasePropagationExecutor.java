package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.propagation.github.UserFollowersFollowingReportToSleepPhasePropagation;
import com.cezarykluczynski.carmen.model.github.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFollowersFollowingReportToSleepPhasePropagationExecutor implements Runnable {

    private UserDAO githubUserDAOImpl;

    private UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase;

    @Autowired
    public UserFollowersFollowingReportToSleepPhasePropagationExecutor(UserDAO githubUserDAOImpl,
           UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase) {
        this.githubUserDAOImpl = githubUserDAOImpl;
        this.propagationUserFollowersFollowingReportToSleepPhase = propagationUserFollowersFollowingReportToSleepPhase;
    }

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
