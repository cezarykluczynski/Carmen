package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowersFollowingReportToSleepPhasePropagationExecutor;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ScheduledUserFollowersFollowingReportToSleepPhasePropagationExecutor {

    @Autowired
    UserFollowersFollowingReportToSleepPhasePropagationExecutor
        userFollowersFollowingReportToSleepPhasePropagationExecutor;

    private TaskExecutor taskExecutor;

    public ScheduledUserFollowersFollowingReportToSleepPhasePropagationExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 2000)
    public void executePropagation() {
        taskExecutor.execute(userFollowersFollowingReportToSleepPhasePropagationExecutor);
    }

}
