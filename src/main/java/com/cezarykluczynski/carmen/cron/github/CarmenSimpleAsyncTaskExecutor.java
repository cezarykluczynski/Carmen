package com.cezarykluczynski.carmen.cron.github;

import com.cezarykluczynski.carmen.cron.management.service.DatabaseSwitchableJobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class CarmenSimpleAsyncTaskExecutor implements TaskExecutor {

    private DatabaseSwitchableJobsService databaseSwitchableJobsService;

    private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor;

    @Autowired
    public CarmenSimpleAsyncTaskExecutor(DatabaseSwitchableJobsService databaseSwitchableJobsService,
        @Qualifier("simpleAsyncTaskExecutor") SimpleAsyncTaskExecutor simpleAsyncTaskExecutor) {
        this.databaseSwitchableJobsService = databaseSwitchableJobsService;
        this.simpleAsyncTaskExecutor = simpleAsyncTaskExecutor;
    }

    @Override
    public void execute(Runnable task) {
        Object noScheduledTasks = System.getProperty("noScheduledTasks");

        if (noScheduledTasks != null ||
                !databaseSwitchableJobsService.isEnabledOrNotDatabaseSwitchable(task.getClass().getSimpleName())) {
            return;
        }

        simpleAsyncTaskExecutor.execute(task);
    }

}
