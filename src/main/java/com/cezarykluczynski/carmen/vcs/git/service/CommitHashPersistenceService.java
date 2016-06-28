package com.cezarykluczynski.carmen.vcs.git.service;

import com.cezarykluczynski.carmen.lang.stats.persistence.Persister;
import com.cezarykluczynski.carmen.model.github.RepositoryClone;
import com.cezarykluczynski.carmen.vcs.git.model.CommitHash;
import com.cezarykluczynski.carmen.vcs.git.model.CommitHashDescriptions;
import com.cezarykluczynski.carmen.vcs.git.persistence.CommitHashDescriptionsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommitHashPersistenceService {

    private CommitHashDescriptionsFactory commitHashDescriptionsFactory;

    private Persister persister;

    @Autowired
    public CommitHashPersistenceService(CommitHashDescriptionsFactory commitHashDescriptionsFactory,
                                        Persister persister) {
        this.commitHashDescriptionsFactory = commitHashDescriptionsFactory;
        this.persister = persister;
    }

    public CommitHashDescriptions persistAndDescribeCommitHashUsingRepositoryClone(CommitHash commitHash,
         RepositoryClone repositoryClone) {
        CommitHashDescriptions commitHashDescriptions = commitHashDescriptionsFactory
                .createUsingRepositoryCloneAndCommitHash(repositoryClone, commitHash);
        persister.persist(commitHashDescriptions.getCommitDescription());
        persister.persist(commitHashDescriptions.getRepositoryDescription());
        return commitHashDescriptions;
    }
}
