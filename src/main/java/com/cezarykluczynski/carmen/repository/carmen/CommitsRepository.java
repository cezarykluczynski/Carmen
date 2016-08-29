package com.cezarykluczynski.carmen.repository.carmen;

import com.cezarykluczynski.carmen.model.cassandra.carmen.Commit;
import com.datastax.driver.mapping.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommitsRepository {

    private Mapper<Commit> commitMapper;

    private CommitAccessor commitAccessor;

    @Autowired
    public CommitsRepository(Mapper<Commit> commitMapper, CommitAccessor commitAccessor) {
        this.commitMapper = commitMapper;
        this.commitAccessor = commitAccessor;
    }

    public Commit findByHash(String hash) {
        return commitAccessor.findByCommitHash(hash);
    }

    public void save(Commit commit) {
        commitMapper.save(commit);
    }

    public void delete(Commit commit) {
        commitMapper.delete(commit);
    }

}
