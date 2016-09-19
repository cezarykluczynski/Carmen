package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository

import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.Repositories
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User

public class RepositoriesRepositoryFixtures {

    private RepositoriesRepository repositoriesRepository

    public RepositoriesRepositoryFixtures(RepositoriesRepository repositoriesRepository) {
        this.repositoriesRepository = repositoriesRepository
    }

    public Repositories createRepositoriesEntityUsingUserEntity(User userEntity) {
        return repositoriesRepository.create(userEntity)
    }

    public void deleteRepositoriesEntity(Repositories repositoriesEntity) {
        repositoriesRepository.delete repositoriesEntity
    }

}
