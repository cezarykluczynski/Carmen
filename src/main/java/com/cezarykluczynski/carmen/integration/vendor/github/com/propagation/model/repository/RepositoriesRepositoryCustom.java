package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.Repositories;

public interface RepositoriesRepositoryCustom {

    Repositories findOldestPropagationInSleepPhase();

    void moveToRefreshPhase(Repositories repositoriesEntity);

    void moveToSleepPhaseUsingUserEntity(User userEntity);

    Repositories create(User userEntity);

}
