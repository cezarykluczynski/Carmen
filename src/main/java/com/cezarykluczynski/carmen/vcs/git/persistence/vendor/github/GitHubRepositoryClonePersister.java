package com.cezarykluczynski.carmen.vcs.git.persistence.vendor.github;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.RepositoryClone;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.RepositoryCloneRepository;
import com.cezarykluczynski.carmen.util.factory.DateFactory;
import com.cezarykluczynski.carmen.vcs.git.model.CommitHash;
import com.cezarykluczynski.carmen.vcs.git.model.CommitHashDescriptions;
import com.cezarykluczynski.carmen.vcs.git.service.CommitHashPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitHubRepositoryClonePersister {

    private CommitHashPersistenceService commitHashPersistenceService;

    private GitHubRepositoryCommitsToPersistFinder gitHubRepositoryCommitsToPersistFinder;

    private RepositoryCloneRepository repositoryCloneRepository;

    private DateFactory dateFactory;

    @Autowired
    public GitHubRepositoryClonePersister(CommitHashPersistenceService commitHashPersistenceService,
            GitHubRepositoryCommitsToPersistFinder gitHubRepositoryCommitsToPersistFinder,
            RepositoryCloneRepository repositoryCloneRepository, DateFactory dateFactory) {
        this.commitHashPersistenceService = commitHashPersistenceService;
        this.gitHubRepositoryCommitsToPersistFinder = gitHubRepositoryCommitsToPersistFinder;
        this.repositoryCloneRepository = repositoryCloneRepository;
        this.dateFactory = dateFactory;
    }

    public void persist() {
        RepositoryClone repositoryClone = repositoryCloneRepository.findRepositoryCloneWithCommitsToPersist();

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
            repositoryCloneRepository.save(repositoryClone);
            return;
        }

        commitHashes.forEach(commitHash -> {
            CommitHashDescriptions commitHashDescriptions = commitHashPersistenceService
                    .persistAndDescribeCommitHashUsingRepositoryClone(commitHash, repositoryClone);
            repositoryClone.setCommitsStatisticsUntil(commitHashDescriptions.getCommitHash().getDate());
        });

        repositoryCloneRepository.save(repositoryClone);
    }

    private List<CommitHash> getCommitsHashFromRepositoryClone(RepositoryClone repositoryClone) {
        return gitHubRepositoryCommitsToPersistFinder.getCommitHashesToPersist(repositoryClone);
    }


}
