package com.cezarykluczynski.carmen.lang.stats.persistence;

import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription;
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription;
import com.cezarykluczynski.carmen.lang.stats.mapper.LinguistDescriptionToEntityMapper;
import com.cezarykluczynski.carmen.model.cassandra.repositories.Commit;
import com.cezarykluczynski.carmen.repository.repositories.CommitsRepository;
import com.datastax.driver.core.utils.UUIDs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CassandraPersister implements Persister {

    private CommitsRepository commitsRepository;

    private LinguistDescriptionToEntityMapper linguistDescriptionToEntityMapper;

    @Autowired
    public CassandraPersister(CommitsRepository commitsRepository,
                              LinguistDescriptionToEntityMapper linguistDescriptionToEntityMapper) {
        this.commitsRepository = commitsRepository;
        this.linguistDescriptionToEntityMapper = linguistDescriptionToEntityMapper;
    }

    public void persist(CommitDescription commitDescription) {
        Commit commit = findOrInitializeCommit(commitDescription.getCommitHash());
        linguistDescriptionToEntityMapper.updateCommitUsingCommitDescription(commit, commitDescription);
        commitsRepository.save(commit);
    }

    public void persist(RepositoryDescription repositoryDescription) {
        Commit commit = findOrInitializeCommit(repositoryDescription.getCommitHash());
        linguistDescriptionToEntityMapper.updateCommitUsingRepositoryDescription(commit, repositoryDescription);
        commitsRepository.save(commit);
    }

    private Commit findOrInitializeCommit(String hash) {
        Commit commit = commitsRepository.findByHash(hash);
        return commit == null ? initializeCommit(hash) : commit;
    }

    private Commit initializeCommit(String hash) {
        Commit commit = new Commit();
        commit.id = UUIDs.random();
        commit.hash = hash;
        return commit;
    }
}
