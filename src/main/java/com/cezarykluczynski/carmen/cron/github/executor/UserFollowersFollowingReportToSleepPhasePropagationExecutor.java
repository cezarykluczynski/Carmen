package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl;
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl;
import com.cezarykluczynski.carmen.propagation.UserFollowersFollowingReportToSleepPhasePropagation;
import com.cezarykluczynski.carmen.model.github.User;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class UserFollowersFollowingReportToSleepPhasePropagationExecutor {

    @Autowired
    UserFollowersDAOImpl propagationsUserFollowersDao;

    @Autowired
    UserFollowingDAOImpl propagationsUserFollowingDao;

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
        } catch (IOException e) {}
    }

}
