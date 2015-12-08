package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.Repository;
import com.cezarykluczynski.carmen.model.github.RepositoryClone;
import com.cezarykluczynski.carmen.vcs.server.Server;

public interface RepositoriesClonesDAO {

    RepositoryClone createStubEntity(Server server, Repository repository);

}
