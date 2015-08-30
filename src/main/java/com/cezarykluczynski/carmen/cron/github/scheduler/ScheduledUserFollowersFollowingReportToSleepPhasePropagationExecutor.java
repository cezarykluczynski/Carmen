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

    private class ScheduledUserFollowersFollowingReportToSleepPhasePropagationExecutorRunnable implements Runnable {

        private UserFollowersFollowingReportToSleepPhasePropagationExecutor
            userFollowersFollowingReportToSleepPhasePropagationExecutor;

        public ScheduledUserFollowersFollowingReportToSleepPhasePropagationExecutorRunnable(
            UserFollowersFollowingReportToSleepPhasePropagationExecutor userFollowersFollowingReportToSleepPhasePropagationExecutor
        ) {
            this.userFollowersFollowingReportToSleepPhasePropagationExecutor =
                userFollowersFollowingReportToSleepPhasePropagationExecutor;
        }

        public void run() {
            userFollowersFollowingReportToSleepPhasePropagationExecutor.run();
        }
    }

    private TaskExecutor taskExecutor;

    public ScheduledUserFollowersFollowingReportToSleepPhasePropagationExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 2000)
    public void executePropagation() {
        Object noTasks = System.getProperty("noScheduledTasks");

        if (noTasks == null) {
            taskExecutor.execute(new ScheduledUserFollowersFollowingReportToSleepPhasePropagationExecutorRunnable(
                userFollowersFollowingReportToSleepPhasePropagationExecutor
            ));
        }
    }

}
