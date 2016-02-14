package com.cezarykluczynski.carmen.cron.github.scheduler

import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor

import com.cezarykluczynski.carmen.cron.github.executor.RepositoriesWakeUpExecutor

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class RepositoriesWakeUpSchedulerTest {

    RepositoriesWakeUpExecutor repositoriesWakeUpExecutor

    RepositoriesWakeUpScheduler repositoriesWakeUpScheduler

    @BeforeMethod
    void setUp() {
        TaskExecutor taskExecutor = new SyncTaskExecutor()
        repositoriesWakeUpExecutor = mock RepositoriesWakeUpExecutor.class
        doNothing().when(repositoriesWakeUpExecutor).run()
        repositoriesWakeUpScheduler = new RepositoriesWakeUpScheduler(taskExecutor, repositoriesWakeUpExecutor)
    }

    @Test
    void scheduledAPIRequestExecutorCallsAPIRequestExecutor() {
        // exercise
        repositoriesWakeUpScheduler.executePropagation()

        // assertion
        verify(repositoriesWakeUpExecutor).run()
    }

}
