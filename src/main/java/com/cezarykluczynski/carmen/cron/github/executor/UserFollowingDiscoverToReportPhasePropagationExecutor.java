package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DatabaseSwitchableJob
public class UserFollowingDiscoverToReportPhasePropagationExecutor implements Runnable {

    private UserFollowingRepository userFollowingRepository;

    private PendingRequestRepository pendingRequestRepository;

    @Autowired
    public UserFollowingDiscoverToReportPhasePropagationExecutor(UserFollowingRepository  userFollowingRepository,
            PendingRequestRepository pendingRequestRepository) {
        this.userFollowingRepository = userFollowingRepository;
        this.pendingRequestRepository = pendingRequestRepository;
    }

    public void run() {
        UserFollowing userFollowing = userFollowingRepository.findOldestPropagationInDiscoverPhase();
        tryToMoveToReportPhase(userFollowing);
    }

    private void tryToMoveToReportPhase(UserFollowing userFollowing) {
        if (userFollowing == null || !userFollowing.getPhase().equals("discover")) {
            return;
        }

        Long propagationId = userFollowing.getId();
        Long count = pendingRequestRepository.countByPropagationId(propagationId);

        if (count > 0) {
            return;
        }

        userFollowing.setPhase("report");
        userFollowingRepository.save(userFollowing);
    }

}
