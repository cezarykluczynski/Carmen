package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowersDiscoverToReportPhasePropagationExecutor;

import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledUserFollowersDiscoverToReportPhasePropagationExecutor {

    @Autowired
    UserFollowersDiscoverToReportPhasePropagationExecutor
        userFollowersDiscoverToReportPhasePropagationExecutor;

    private class ScheduledUserFollowersDiscoverToReportPhasePropagationExecutorRunnable implements Runnable {

        private UserFollowersDiscoverToReportPhasePropagationExecutor
            userFollowersDiscoverToReportPhasePropagationExecutor;

        public ScheduledUserFollowersDiscoverToReportPhasePropagationExecutorRunnable(
            UserFollowersDiscoverToReportPhasePropagationExecutor userFollowersDiscoverToReportPhasePropagationExecutor
        ) {
            this.userFollowersDiscoverToReportPhasePropagationExecutor =
                userFollowersDiscoverToReportPhasePropagationExecutor;
        }

        public void run() {
            userFollowersDiscoverToReportPhasePropagationExecutor.run();
        }
    }

    private TaskExecutor taskExecutor;

    public ScheduledUserFollowersDiscoverToReportPhasePropagationExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 2000)
    public void executePropagation() {
        taskExecutor.execute(new ScheduledUserFollowersDiscoverToReportPhasePropagationExecutorRunnable(
            userFollowersDiscoverToReportPhasePropagationExecutor
        ));
    }

}
