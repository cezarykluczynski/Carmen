package com.cezarykluczynski.carmen.cron;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledAPIRequestExecutor {

    @Autowired
    APIRequestExecutor apiRequestExecutor;

    private class ScheduledAPIRequestExecutorRunnable implements Runnable {

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
        taskExecutor.execute(new ScheduledAPIRequestExecutorRunnable(apiRequestExecutor));
    }

}