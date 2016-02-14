package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.APIRequestExecutor;

public class APIRequestScheduler {

    private TaskExecutor taskExecutor;

    private APIRequestExecutor apiRequestExecutor;

    public APIRequestScheduler(TaskExecutor taskExecutor, APIRequestExecutor apiRequestExecutor) {
        this.taskExecutor = taskExecutor;
        this.apiRequestExecutor = apiRequestExecutor;
    }

    @Scheduled(fixedDelay = 5000)
    public void executePropagation() {
        taskExecutor.execute(apiRequestExecutor);
    }

}
