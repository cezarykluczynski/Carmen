package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowingDiscoverToReportPhasePropagationExecutor;

import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledUserFollowingDiscoverToReportPhasePropagationExecutor {

    @Autowired
    UserFollowingDiscoverToReportPhasePropagationExecutor userFollowingDiscoverToReportPhasePropagationExecutor;


    private TaskExecutor taskExecutor;

    public ScheduledUserFollowingDiscoverToReportPhasePropagationExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 2000)
    public void executePropagation() {
        taskExecutor.execute(userFollowingDiscoverToReportPhasePropagationExecutor);
    }

}
