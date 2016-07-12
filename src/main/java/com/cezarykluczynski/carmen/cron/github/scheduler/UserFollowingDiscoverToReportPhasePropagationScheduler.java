package com.cezarykluczynski.carmen.cron.github.scheduler;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowingDiscoverToReportPhasePropagationExecutor;
import org.springframework.stereotype.Component;

@Component
@DatabaseSwitchableJob
public class UserFollowingDiscoverToReportPhasePropagationScheduler {

    private TaskExecutor taskExecutor;

    private UserFollowingDiscoverToReportPhasePropagationExecutor propagationExecutor;

    @Autowired
    public UserFollowingDiscoverToReportPhasePropagationScheduler(TaskExecutor taskExecutor,
            UserFollowingDiscoverToReportPhasePropagationExecutor propagationExecutor) {
        this.taskExecutor = taskExecutor;
        this.propagationExecutor = propagationExecutor;
    }

    @Scheduled(fixedDelay = 2000)
    public void executePropagation() {
        taskExecutor.execute(propagationExecutor);
    }

}
