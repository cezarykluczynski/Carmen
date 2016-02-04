package com.cezarykluczynski.carmen.cron.github;

import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@Profile("github-api-cron")
public class GitHubSimpleAsyncTaskExecutor extends SimpleAsyncTaskExecutor {

    @Override
    public void execute(Runnable task) {
        Object noTasks = System.getProperty("noScheduledTasks");

        if (noTasks == null) {
            super.execute(task);
        }
    }

}
