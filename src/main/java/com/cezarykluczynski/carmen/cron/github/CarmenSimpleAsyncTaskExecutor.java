package com.cezarykluczynski.carmen.cron.github;

import org.springframework.core.task.SimpleAsyncTaskExecutor;

public class CarmenSimpleAsyncTaskExecutor extends SimpleAsyncTaskExecutor {

    @Override
    public void execute(Runnable task) {
        Object noTasks = System.getProperty("noScheduledTasks");

        if (noTasks == null) {
            super.execute(task);
        }
    }

}
