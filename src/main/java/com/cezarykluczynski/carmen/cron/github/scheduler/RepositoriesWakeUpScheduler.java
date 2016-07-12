package com.cezarykluczynski.carmen.cron.github.scheduler;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.RepositoriesWakeUpExecutor;
import org.springframework.stereotype.Component;

@Component
@DatabaseSwitchableJob
public class RepositoriesWakeUpScheduler {

    private TaskExecutor taskExecutor;

    private RepositoriesWakeUpExecutor repositoriesWakeUpExecutor;

    @Autowired
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
