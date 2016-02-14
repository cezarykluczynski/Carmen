package com.cezarykluczynski.carmen.cron.github.scheduler

import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowersFollowingReportToSleepPhasePropagationExecutor

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class UserFollowersFollowingReportToSleepPhasePropagationSchedulerTest {

    UserFollowersFollowingReportToSleepPhasePropagationExecutor propagationExecutor

    UserFollowersFollowingReportToSleepPhasePropagationScheduler propagationScheduler

    @BeforeMethod
    void setUp() {
        TaskExecutor taskExecutor = new SyncTaskExecutor()
        propagationExecutor = mock UserFollowersFollowingReportToSleepPhasePropagationExecutor.class
        doNothing().when(propagationExecutor).run()
        propagationScheduler = new UserFollowersFollowingReportToSleepPhasePropagationScheduler(taskExecutor, propagationExecutor)
    }

    @Test
    void scheduledUserFollowersFollowingReportToSleepPhasePropagationExecutor() {
        // exercise
        propagationScheduler.executePropagation()

        // assertion
        verify(propagationExecutor).run()
    }

}
