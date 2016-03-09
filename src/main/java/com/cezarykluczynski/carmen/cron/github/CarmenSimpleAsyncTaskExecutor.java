package com.cezarykluczynski.carmen.cron.github;

import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

@Service("apiTaskExecutor")
@Profile("github-api-cron")
public class CarmenSimpleAsyncTaskExecutor extends SimpleAsyncTaskExecutor {

    @Override
    public void execute(Runnable task) {
        Object noTasks = System.getProperty("noScheduledTasks");

        if (noTasks == null) {
            super.execute(task);
        }
    }

}
