package com.cezarykluczynski.carmen.vcs.git.persistence;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import com.cezarykluczynski.carmen.dao.github.RepositoriesClonesDAO;
import com.cezarykluczynski.carmen.model.github.RepositoryClone;
import com.cezarykluczynski.carmen.util.factory.DateFactory;
import com.cezarykluczynski.carmen.vcs.git.model.CommitHash;
import com.cezarykluczynski.carmen.vcs.git.model.CommitHashDescriptions;
import com.cezarykluczynski.carmen.vcs.git.service.CommitHashPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DatabaseSwitchableJob
public class GitHubRepositoryClonePersister {

    private CommitHashPersistenceService commitHashPersistenceService;

    private GitHubRepositoryCommitsToPersistFinder gitHubRepositoryCommitsToPersistFinder;

    private RepositoriesClonesDAO repositoriesClonesDAO;

    private DateFactory dateFactory;

    @Autowired
    public GitHubRepositoryClonePersister(CommitHashPersistenceService commitHashPersistenceService,
            GitHubRepositoryCommitsToPersistFinder gitHubRepositoryCommitsToPersistFinder,
            RepositoriesClonesDAO repositoriesClonesDAO, DateFactory dateFactory) {
        this.commitHashPersistenceService = commitHashPersistenceService;
        this.gitHubRepositoryCommitsToPersistFinder = gitHubRepositoryCommitsToPersistFinder;
        this.repositoriesClonesDAO = repositoriesClonesDAO;
        this.dateFactory = dateFactory;
    }

    public void persist() {
        RepositoryClone repositoryClone = repositoriesClonesDAO.findRepositoryCloneWithCommitsToPersist();

        if (repositoryClone != null) {
            doPersist(repositoryClone);
        }
    }

    private void doPersist(RepositoryClone repositoryClone) {
        List<CommitHash> commitHashes = getCommitsHashFromRepositoryClone(repositoryClone);

        if (commitHashes == null) {
            return;
        } else if (commitHashes.isEmpty()) {
            repositoryClone.setCommitsStatisticsUntil(dateFactory.getBeginningOfNotYetOpenedMonth());
            repositoriesClonesDAO.update(repositoryClone);
            return;
        }

        commitHashes.forEach(commitHash -> {
            CommitHashDescriptions commitHashDescriptions = commitHashPersistenceService
                    .persistAndDescribeCommitHashUsingRepositoryClone(commitHash, repositoryClone);
            repositoryClone.setCommitsStatisticsUntil(commitHashDescriptions.getCommitHash().getDate());
        });

        repositoriesClonesDAO.update(repositoryClone);
    }

    private List<CommitHash> getCommitsHashFromRepositoryClone(RepositoryClone repositoryClone) {
        return gitHubRepositoryCommitsToPersistFinder.getCommitHashesToPersist(repositoryClone);
    }


}
