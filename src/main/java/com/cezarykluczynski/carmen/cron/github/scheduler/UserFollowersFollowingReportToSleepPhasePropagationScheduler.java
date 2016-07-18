package com.cezarykluczynski.carmen.cron.github.scheduler;

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowersFollowingReportToSleepPhasePropagationExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserFollowersFollowingReportToSleepPhasePropagationScheduler {

    private TaskExecutor taskExecutor;

    private UserFollowersFollowingReportToSleepPhasePropagationExecutor propagationExecutor;

    @Autowired
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
