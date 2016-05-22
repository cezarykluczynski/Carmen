package com.cezarykluczynski.carmen.lang.stats.persistence;

import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription;
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription;

public interface Persister {

    void persist(CommitDescription commitDescription);

    void persist(RepositoryDescription repositoryDescription);

}
