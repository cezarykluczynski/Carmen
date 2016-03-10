package com.cezarykluczynski.carmen.cron.github;

import org.springframework.core.task.SimpleAsyncTaskExecutor;

public class CarmenNoopTaskExecutor extends SimpleAsyncTaskExecutor {

    @Override
    public void execute(Runnable task) {
    }

}
