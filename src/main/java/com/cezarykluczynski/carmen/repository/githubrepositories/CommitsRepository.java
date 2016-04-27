package com.cezarykluczynski.carmen.repository.githubrepositories;

import com.cezarykluczynski.carmen.model.cassandra.github_repositories.Commit;
import org.springframework.data.repository.CrudRepository;

public interface CommitsRepository extends CrudRepository<Commit, Long> {
}
