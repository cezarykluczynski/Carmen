package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.RepositoriesWakeUpExecutor;

import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledRepositoriesWakeUpExecutor {

    @Autowired
    RepositoriesWakeUpExecutor repositoriesWakeUpExecutor;

    private TaskExecutor taskExecutor;

    public ScheduledRepositoriesWakeUpExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 15000)
    public void executePropagation() {
        taskExecutor.execute(repositoriesWakeUpExecutor);
    }

}
