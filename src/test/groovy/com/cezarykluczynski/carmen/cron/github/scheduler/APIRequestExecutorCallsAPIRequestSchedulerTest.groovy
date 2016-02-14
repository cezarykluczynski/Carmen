package com.cezarykluczynski.carmen.cron.github.scheduler

import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor

import com.cezarykluczynski.carmen.cron.github.executor.APIRequestExecutor

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class APIRequestExecutorCallsAPIRequestSchedulerTest {

    APIRequestExecutor apiRequestExecutor

    APIRequestScheduler apiRequestScheduler

    @BeforeMethod
    void setUp() {
        TaskExecutor taskExecutor = new SyncTaskExecutor()
        apiRequestExecutor = mock APIRequestExecutor.class
        doNothing().when(apiRequestExecutor).run()
        apiRequestScheduler = new APIRequestScheduler(taskExecutor, apiRequestExecutor)
    }

    @Test
    void scheduledAPIRequestExecutorCallsAPIRequestExecutor() {
        // exercise
        apiRequestScheduler.executePropagation()

        // assertion
        verify(apiRequestExecutor).run()
    }

}
