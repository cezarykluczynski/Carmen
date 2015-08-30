package com.cezarykluczynski.carmen.cron.github.scheduler;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;

import com.cezarykluczynski.carmen.cron.github.executor.APIRequestExecutor;

import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledAPIRequestExecutor {

    @Autowired
    APIRequestExecutor apiRequestExecutor;

    public class ScheduledAPIRequestExecutorRunnable implements Runnable {

        private APIRequestExecutor apiRequestExecutor;

        public ScheduledAPIRequestExecutorRunnable(APIRequestExecutor apiRequestExecutor) {
            this.apiRequestExecutor = apiRequestExecutor;
        }

        public void run() {
            apiRequestExecutor.run();
        }
    }

    private TaskExecutor taskExecutor;

    public ScheduledAPIRequestExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 5000)
    public void executePropagation() {
        Object noTasks = System.getProperty("noScheduledTasks");

        if (noTasks == null) {
            taskExecutor.execute(new ScheduledAPIRequestExecutorRunnable(apiRequestExecutor));
        }
    }

}
