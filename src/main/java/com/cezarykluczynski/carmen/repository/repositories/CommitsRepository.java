package com.cezarykluczynski.carmen.repository.repositories;

import com.cezarykluczynski.carmen.model.cassandra.repositories.Commit;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CommitsRepository extends CrudRepository<Commit, Long> {

    @Query("select * from repositories.commits where hash = ?0")
    Commit findByHash(String hash);

}
