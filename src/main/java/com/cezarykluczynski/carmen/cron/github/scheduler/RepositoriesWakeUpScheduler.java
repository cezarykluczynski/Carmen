package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.RepositoriesWakeUpExecutor;

public class RepositoriesWakeUpScheduler {

    private TaskExecutor taskExecutor;

    private RepositoriesWakeUpExecutor repositoriesWakeUpExecutor;

    public RepositoriesWakeUpScheduler(TaskExecutor taskExecutor,
            RepositoriesWakeUpExecutor repositoriesWakeUpExecutor) {
        this.taskExecutor = taskExecutor;
        this.repositoriesWakeUpExecutor = repositoriesWakeUpExecutor;
    }

    @Scheduled(fixedDelay = 15000)
    public void executePropagation() {
        taskExecutor.execute(repositoriesWakeUpExecutor);
    }

}
