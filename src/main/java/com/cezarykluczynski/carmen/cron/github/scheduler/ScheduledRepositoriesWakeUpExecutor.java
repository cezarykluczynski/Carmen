package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.RepositoriesWakeUpExecutor;

import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledRepositoriesWakeUpExecutor {

    @Autowired
    RepositoriesWakeUpExecutor repositoriesWakeUpExecutor;

    public class ScheduledRepositoriesWakeUpExecutorRunnable implements Runnable {

        private RepositoriesWakeUpExecutor repositoriesWakeUpExecutor;

        public ScheduledRepositoriesWakeUpExecutorRunnable(RepositoriesWakeUpExecutor repositoriesWakeUpExecutor) {
            this.repositoriesWakeUpExecutor = repositoriesWakeUpExecutor;
        }

        public void run() {
            repositoriesWakeUpExecutor.run();
        }
    }

    private TaskExecutor taskExecutor;

    public ScheduledRepositoriesWakeUpExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 15000)
    public void executePropagation() {
        taskExecutor.execute(new ScheduledRepositoriesWakeUpExecutorRunnable(repositoriesWakeUpExecutor));
    }

}
