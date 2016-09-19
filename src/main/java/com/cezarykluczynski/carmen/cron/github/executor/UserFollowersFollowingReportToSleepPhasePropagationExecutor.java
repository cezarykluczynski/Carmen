package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository;
import com.cezarykluczynski.carmen.propagation.github.UserFollowersFollowingReportToSleepPhasePropagation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DatabaseSwitchableJob
public class UserFollowersFollowingReportToSleepPhasePropagationExecutor implements Runnable {

    private UserRepository userRepository;

    private UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase;

    @Autowired
    public UserFollowersFollowingReportToSleepPhasePropagationExecutor(UserRepository userRepository,
           UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase) {
        this.userRepository = userRepository;
        this.propagationUserFollowersFollowingReportToSleepPhase = propagationUserFollowersFollowingReportToSleepPhase;
    }

    public void run() {
        try {
            User userEntity = userRepository.findUserInReportFollowersFolloweesPhase();

            if (userEntity != null) {
                propagationUserFollowersFollowingReportToSleepPhase.setUserEntity(userEntity);
                propagationUserFollowersFollowingReportToSleepPhase.propagate();
            }
        } catch (Exception e) {}
    }

}
