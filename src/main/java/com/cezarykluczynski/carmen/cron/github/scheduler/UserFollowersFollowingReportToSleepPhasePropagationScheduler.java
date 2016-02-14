package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowersFollowingReportToSleepPhasePropagationExecutor;

public class UserFollowersFollowingReportToSleepPhasePropagationScheduler {

    private TaskExecutor taskExecutor;

    private UserFollowersFollowingReportToSleepPhasePropagationExecutor propagationExecutor;

    public UserFollowersFollowingReportToSleepPhasePropagationScheduler(TaskExecutor taskExecutor,
            UserFollowersFollowingReportToSleepPhasePropagationExecutor propagationExecutor) {
        this.taskExecutor = taskExecutor;
        this.propagationExecutor = propagationExecutor;
    }

    @Scheduled(fixedDelay = 2000)
    public void executePropagation() {
        taskExecutor.execute(propagationExecutor);
    }

}
