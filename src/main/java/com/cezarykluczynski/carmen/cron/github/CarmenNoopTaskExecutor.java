package com.cezarykluczynski.carmen.cron.github;

import org.springframework.context.annotation.Profile;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;

@Service("apiTaskExecutor")
@Profile("!github-api-cron")
public class CarmenNoopTaskExecutor extends SimpleAsyncTaskExecutor {

    @Override
    public void execute(Runnable task) {
    }

}
