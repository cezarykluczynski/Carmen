package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowingDiscoverToReportPhasePropagationExecutor;

import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledUserFollowingDiscoverToReportPhasePropagationExecutor {

    @Autowired
    UserFollowingDiscoverToReportPhasePropagationExecutor
        userFollowingDiscoverToReportPhasePropagationExecutor;

    private class ScheduledUserFollowingDiscoverToReportPhasePropagationExecutorRunnable implements Runnable {

        private UserFollowingDiscoverToReportPhasePropagationExecutor
            userFollowingDiscoverToReportPhasePropagationExecutor;

        public ScheduledUserFollowingDiscoverToReportPhasePropagationExecutorRunnable(
            UserFollowingDiscoverToReportPhasePropagationExecutor userFollowingDiscoverToReportPhasePropagationExecutor
        ) {
            this.userFollowingDiscoverToReportPhasePropagationExecutor =
                userFollowingDiscoverToReportPhasePropagationExecutor;
        }

        public void run() {
            userFollowingDiscoverToReportPhasePropagationExecutor.run();
        }
    }

    private TaskExecutor taskExecutor;

    public ScheduledUserFollowingDiscoverToReportPhasePropagationExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 2000)
    public void executePropagation() {
        Object noTasks = System.getProperty("noScheduledTasks");

        System.out.println("Following");

        if (noTasks == null) {
            taskExecutor.execute(new ScheduledUserFollowingDiscoverToReportPhasePropagationExecutorRunnable(
                userFollowingDiscoverToReportPhasePropagationExecutor
            ));
        }
    }

}
