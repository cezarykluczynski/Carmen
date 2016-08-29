package com.cezarykluczynski.carmen.repository.carmen;

import com.cezarykluczynski.carmen.model.cassandra.carmen.Commit;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface CommitAccessor {

    @Query("select * from carmen.commits where hash = :hash")
    Commit findByCommitHash(@Param("hash") String hash);

}
