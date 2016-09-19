package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DatabaseSwitchableJob
public class UserFollowersDiscoverToReportPhasePropagationExecutor implements Runnable {

    private UserFollowersRepository userFollowersRepository;

    private PendingRequestRepository pendingRequestRepository;

    @Autowired
    public UserFollowersDiscoverToReportPhasePropagationExecutor(UserFollowersRepository userFollowersRepository,
            PendingRequestRepository pendingRequestRepository) {
        this.userFollowersRepository = userFollowersRepository;
        this.pendingRequestRepository = pendingRequestRepository;
    }

    public void run() {
        UserFollowers userFollowers = userFollowersRepository.findOldestPropagationInDiscoverPhase();
        tryToMoveToReportPhase(userFollowers);
    }

    private void tryToMoveToReportPhase(UserFollowers userFollowers) {
        if (userFollowers == null || !userFollowers.getPhase().equals("discover")) {
            return;
        }

        Long propagationId = userFollowers.getId();
        Long count = pendingRequestRepository.countByPropagationId(propagationId);

        if (count > 0) {
            return;
        }

        userFollowers.setPhase("report");
        userFollowersRepository.save(userFollowers);
    }

}
