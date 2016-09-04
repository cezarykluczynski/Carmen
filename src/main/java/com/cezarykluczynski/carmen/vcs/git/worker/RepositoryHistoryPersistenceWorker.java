package com.cezarykluczynski.carmen.vcs.git.worker;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import com.cezarykluczynski.carmen.vcs.git.persistence.vendor.github.GitHubRepositoryClonePersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DatabaseSwitchableJob
public class RepositoryHistoryPersistenceWorker implements Runnable {

    private GitHubRepositoryClonePersister gitHubRepositoryClonePersister;

    @Autowired
    public RepositoryHistoryPersistenceWorker(GitHubRepositoryClonePersister gitHubRepositoryClonePersister) {
        this.gitHubRepositoryClonePersister = gitHubRepositoryClonePersister;
    }

    @Override
    public void run() {
        gitHubRepositoryClonePersister.persist();
    }
}
