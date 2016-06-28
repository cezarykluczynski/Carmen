package com.cezarykluczynski.carmen.vcs.git

import com.cezarykluczynski.carmen.cron.github.executor.RepositoriesWakeUpExecutor
import com.cezarykluczynski.carmen.cron.github.scheduler.RepositoriesWakeUpScheduler
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

class GitHubCloneSchedulerTest {

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


