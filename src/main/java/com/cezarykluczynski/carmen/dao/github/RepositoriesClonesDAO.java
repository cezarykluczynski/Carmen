package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.Repository;
import com.cezarykluczynski.carmen.model.github.RepositoryClone;
import com.cezarykluczynski.carmen.vcs.server.Server;

public interface RepositoriesClonesDAO {

    RepositoryClone createStubEntity(Server server, Repository repositoryEntity);

    RepositoryClone truncateEntity(Server server, RepositoryClone repositoryCloneEntity);

    RepositoryClone findByRepositoryEntity(Repository repositoryEntity);

    void setStatusToCloned(RepositoryClone repositoryEntity);

    RepositoryClone create(RepositoryClone repositoryCloneEntity);

    RepositoryClone update(RepositoryClone repositoryCloneEntity);
}
