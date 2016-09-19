package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User

class RepositoryRepositoryFixtures {

    private RepositoryRepository repositoryRepository

    public RepositoryRepositoryFixtures(RepositoryRepository repositoryRepository) {
        this.repositoryRepository = repositoryRepository
    }

    public Repository createRandomEntityUsingUserEntity(User userEntity) {
        Repository repositoryEntity = new Repository()
        repositoryEntity.setUser userEntity

        repositoryRepository.save repositoryEntity

        return repositoryEntity
    }

}
