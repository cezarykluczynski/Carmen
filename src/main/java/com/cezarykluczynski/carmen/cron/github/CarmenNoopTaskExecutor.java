package com.cezarykluczynski.carmen.cron.github;

import org.springframework.core.task.TaskExecutor;

public class CarmenNoopTaskExecutor implements TaskExecutor {

    @Override
    public void execute(Runnable task) {
    }

}
