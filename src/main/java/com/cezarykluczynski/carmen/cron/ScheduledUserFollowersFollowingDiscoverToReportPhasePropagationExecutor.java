package com.cezarykluczynski.carmen.cron;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledUserFollowersFollowingDiscoverToReportPhasePropagationExecutor {

    @Autowired
    UserFollowersFollowingDiscoverToReportPhasePropagationExecutor
        userFollowersFollowingDiscoverToReportPhasePropagationExecutor;

    private class ScheduledUserFollowersFollowingDiscoverToReportPhasePropagationExecutorRunnable implements Runnable {

        private UserFollowersFollowingDiscoverToReportPhasePropagationExecutor
            userFollowersFollowingDiscoverToReportPhasePropagationExecutor;

        public ScheduledUserFollowersFollowingDiscoverToReportPhasePropagationExecutorRunnable(
            UserFollowersFollowingDiscoverToReportPhasePropagationExecutor userFollowersFollowingDiscoverToReportPhasePropagationExecutor
        ) {
            this.userFollowersFollowingDiscoverToReportPhasePropagationExecutor =
                userFollowersFollowingDiscoverToReportPhasePropagationExecutor;
        }

        public void run() {
            userFollowersFollowingDiscoverToReportPhasePropagationExecutor.run();
        }
    }

    private TaskExecutor taskExecutor;

    public ScheduledUserFollowersFollowingDiscoverToReportPhasePropagationExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 3000)
    public void executePropagation() {
        Object noTasks = System.getProperty("noScheduledTasks");

        if (noTasks == null) {
            taskExecutor.execute(new ScheduledUserFollowersFollowingDiscoverToReportPhasePropagationExecutorRunnable(
                userFollowersFollowingDiscoverToReportPhasePropagationExecutor
            ));
        }
    }

}