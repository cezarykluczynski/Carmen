package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.APIRequestExecutor;

import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledAPIRequestExecutor {

    @Autowired
    APIRequestExecutor apiRequestExecutor;

    private TaskExecutor taskExecutor;

    public ScheduledAPIRequestExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 5000)
    public void executePropagation() {
        taskExecutor.execute(apiRequestExecutor);
    }

}
