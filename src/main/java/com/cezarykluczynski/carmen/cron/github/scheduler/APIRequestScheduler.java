package com.cezarykluczynski.carmen.cron.github.scheduler;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.APIRequestExecutor;
import org.springframework.stereotype.Component;

@Component
@DatabaseSwitchableJob
public class APIRequestScheduler {

    private TaskExecutor taskExecutor;

    private APIRequestExecutor apiRequestExecutor;

    @Autowired
    public APIRequestScheduler(TaskExecutor taskExecutor, APIRequestExecutor apiRequestExecutor) {
        this.taskExecutor = taskExecutor;
        this.apiRequestExecutor = apiRequestExecutor;
    }

    @Scheduled(fixedDelay = 5000)
    public void executePropagation() {
        taskExecutor.execute(apiRequestExecutor);
    }

}
