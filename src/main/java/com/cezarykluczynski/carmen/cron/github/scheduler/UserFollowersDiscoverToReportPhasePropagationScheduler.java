package com.cezarykluczynski.carmen.cron.github.scheduler;

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowersDiscoverToReportPhasePropagationExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserFollowersDiscoverToReportPhasePropagationScheduler {

    private TaskExecutor taskExecutor;

    private UserFollowersDiscoverToReportPhasePropagationExecutor propagationExecutor;

    @Autowired
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
