package com.cezarykluczynski.carmen.vcs.git.scheduler

import com.cezarykluczynski.carmen.vcs.git.worker.RepositoryHistoryPersistenceWorker
import org.springframework.core.task.SyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.*

class RepositoryHistoryPersistenceSchedulerTest {

    RepositoryHistoryPersistenceWorker repositoryHistoryPersistenceWorker

    RepositoryHistoryPersistenceScheduler repositoryHistoryPeristenceScheduler

    @BeforeMethod
    void setUp() {
        TaskExecutor taskExecutor = new SyncTaskExecutor()
        repositoryHistoryPersistenceWorker = mock RepositoryHistoryPersistenceWorker.class
        doNothing().when(repositoryHistoryPersistenceWorker).run()
        repositoryHistoryPeristenceScheduler =
                new RepositoryHistoryPersistenceScheduler(taskExecutor, repositoryHistoryPersistenceWorker)
    }

    @Test
    void scheduledAPIRequestExecutorCallsAPIRequestExecutor() {
        // exercise
        repositoryHistoryPeristenceScheduler.executePropagation()

        // assertion
        verify(repositoryHistoryPersistenceWorker).run()
    }
}
