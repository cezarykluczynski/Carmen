package com.cezarykluczynski.carmen.vcs.git.scheduler

import com.cezarykluczynski.carmen.vcs.git.worker.RepositoryHistoryPersistenceWorker
import org.springframework.core.task.TaskExecutor
import spock.lang.Specification

class RepositoryHistoryPersistenceSchedulerTest extends Specification {

    private TaskExecutor taskExecutorMock

    private RepositoryHistoryPersistenceWorker repositoryHistoryPersistenceWorkerMock

    private RepositoryHistoryPersistenceScheduler repositoryHistoryPersistenceScheduler

    void setup() {
        taskExecutorMock = Mock TaskExecutor
        repositoryHistoryPersistenceWorkerMock = Mock RepositoryHistoryPersistenceWorker
        repositoryHistoryPersistenceScheduler = new RepositoryHistoryPersistenceScheduler(taskExecutorMock,
                repositoryHistoryPersistenceWorkerMock)
    }

    void scheduledAPIRequestExecutorCallsAPIRequestExecutor() {
        when:
        repositoryHistoryPersistenceScheduler.executePropagation()

        then:
        1 * taskExecutorMock.execute(repositoryHistoryPersistenceWorkerMock)
    }
}
