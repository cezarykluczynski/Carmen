package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowersDiscoverToReportPhasePropagationExecutor;

public class UserFollowersDiscoverToReportPhasePropagationScheduler {

    private TaskExecutor taskExecutor;

    private UserFollowersDiscoverToReportPhasePropagationExecutor propagationExecutor;

    public UserFollowersDiscoverToReportPhasePropagationScheduler(TaskExecutor taskExecutor,
            UserFollowersDiscoverToReportPhasePropagationExecutor propagationExecutor) {
        this.taskExecutor = taskExecutor;
        this.propagationExecutor = propagationExecutor;
    }

    @Scheduled(fixedDelay = 2000)
    public void executePropagation() {
        taskExecutor.execute(propagationExecutor);
    }

}
