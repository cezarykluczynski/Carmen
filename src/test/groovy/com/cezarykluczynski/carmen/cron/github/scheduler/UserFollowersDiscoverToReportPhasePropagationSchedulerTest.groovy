package com.cezarykluczynski.carmen.cron.github.scheduler

import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowersDiscoverToReportPhasePropagationExecutor

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class UserFollowersDiscoverToReportPhasePropagationSchedulerTest {

    UserFollowersDiscoverToReportPhasePropagationExecutor propagationExecutor

    UserFollowersDiscoverToReportPhasePropagationScheduler propagationScheduler

    @BeforeMethod
    void setUp() {
        TaskExecutor taskExecutor = new SyncTaskExecutor()
        propagationExecutor = mock UserFollowersDiscoverToReportPhasePropagationExecutor.class
        doNothing().when(propagationExecutor).run()
        propagationScheduler = new UserFollowersDiscoverToReportPhasePropagationScheduler(taskExecutor, propagationExecutor)
    }

    @Test
    void scheduledAPIRequestExecutorCallsAPIRequestExecutor() {
        // exercise
        propagationScheduler.executePropagation()

        // assertion
        verify(propagationExecutor).run()
    }

}
