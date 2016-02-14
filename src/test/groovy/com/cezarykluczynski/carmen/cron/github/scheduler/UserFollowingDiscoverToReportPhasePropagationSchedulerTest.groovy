package com.cezarykluczynski.carmen.cron.github.scheduler

import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowingDiscoverToReportPhasePropagationExecutor

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class UserFollowingDiscoverToReportPhasePropagationSchedulerTest {

    UserFollowingDiscoverToReportPhasePropagationExecutor propagationExecutor

    UserFollowingDiscoverToReportPhasePropagationScheduler propagationScheduler

    @BeforeMethod
    void setUp() {
        TaskExecutor taskExecutor = new SyncTaskExecutor()
        propagationExecutor = mock UserFollowingDiscoverToReportPhasePropagationExecutor.class
        doNothing().when(propagationExecutor).run()
        propagationScheduler = new UserFollowingDiscoverToReportPhasePropagationScheduler(taskExecutor, propagationExecutor)
    }

    @Test
    void scheduledAPIRequestExecutorCallsAPIRequestExecutor() {
        // exercise
        propagationScheduler.executePropagation()

        // assertion
        verify(propagationExecutor).run()
    }

}
