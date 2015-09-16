package com.cezarykluczynski.carmen.cron.github.scheduler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.cron.github.executor.UserFollowersFollowingReportToSleepPhasePropagationExecutor

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
class ScheduledUserFollowersFollowingReportToSleepPhasePropagationExecutorTest extends AbstractTestNGSpringContextTests {

    @Mock
    UserFollowersFollowingReportToSleepPhasePropagationExecutor userFollowersFollowingReportToSleepPhasePropagationExecutor

    @Autowired
    @InjectMocks
    ScheduledUserFollowersFollowingReportToSleepPhasePropagationExecutor scheduledUserFollowersFollowingReportToSleepPhasePropagationExecutor

    def noTasks

    @BeforeMethod
    void setUp() {
        noTasks = System.getProperty "noScheduledTasks"
        System.clearProperty "noScheduledTasks"
        MockitoAnnotations.initMocks this
        doNothing().when(userFollowersFollowingReportToSleepPhasePropagationExecutor).run()
    }

    @Test
    void scheduledUserFollowersFollowingReportToSleepPhasePropagationExecutor() {
        // exercise
        scheduledUserFollowersFollowingReportToSleepPhasePropagationExecutor.executePropagation()
        /* Probably because the org.springframework.core.task.TaskExecutor, that is a dependency
           for com.cezarykluczynski.carmen.cron.github.scheduler.ScheduledAPIRequestExecutor class runs on different thread,
           verification of userFollowersFollowingReportToSleepPhasePropagationExecutor.run() would fail if we wouldn't wait a tiny bit.
           This can be tuned to a few more milliseconds if it fails for anyone. */
        Thread.sleep 10

        // assertion
        verify(userFollowersFollowingReportToSleepPhasePropagationExecutor).run()
    }

    @AfterMethod
    void tearDown() {
        System.setProperty "noScheduledTasks", noTasks
        Mockito.reset userFollowersFollowingReportToSleepPhasePropagationExecutor
    }

}
