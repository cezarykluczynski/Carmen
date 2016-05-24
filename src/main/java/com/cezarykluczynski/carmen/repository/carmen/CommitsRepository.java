package com.cezarykluczynski.carmen.repository.carmen;

import com.cezarykluczynski.carmen.model.cassandra.carmen.Commit;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CommitsRepository extends CrudRepository<Commit, Long> {

    @Query("select * from commits where hash = ?0")
    Commit findByHash(String hash);

}
