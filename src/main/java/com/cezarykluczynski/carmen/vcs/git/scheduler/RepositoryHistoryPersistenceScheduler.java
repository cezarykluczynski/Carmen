package com.cezarykluczynski.carmen.vcs.git.scheduler;

import com.cezarykluczynski.carmen.vcs.git.worker.RepositoryHistoryPersistenceWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RepositoryHistoryPersistenceScheduler {

    private TaskExecutor taskExecutor;

    private RepositoryHistoryPersistenceWorker repositoryHistoryPersistenceWorker;

    @Autowired
    public RepositoryHistoryPersistenceScheduler(TaskExecutor taskExecutor,
        RepositoryHistoryPersistenceWorker repositoryHistoryPersistenceWorker) {
        this.taskExecutor = taskExecutor;
        this.repositoryHistoryPersistenceWorker = repositoryHistoryPersistenceWorker;
    }

    @Scheduled(fixedDelay = 5000)
    public void executePropagation() {
        taskExecutor.execute(repositoryHistoryPersistenceWorker);
    }

}
