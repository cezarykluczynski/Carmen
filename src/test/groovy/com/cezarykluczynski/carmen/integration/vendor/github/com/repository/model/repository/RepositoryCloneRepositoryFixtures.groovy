package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.RepositoryClone
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.vcs.server.Server

import javax.persistence.EntityManager

class RepositoryCloneRepositoryFixtures {

    private UserRepositoryFixtures userRepositoryFixtures

    private EntityManager entityManager

    private RepositoryCloneRepository repositoryCloneRepository

    private RepositoryRepository repositoryRepository

    private Server server

    public RepositoryCloneRepositoryFixtures(UserRepositoryFixtures userRepositoryFixtures,
            RepositoryCloneRepository repositoryCloneRepository, RepositoryRepository repositoryRepository,
            Server server, EntityManager entityManager) {
        this.userRepositoryFixtures = userRepositoryFixtures
        this.repositoryCloneRepository = repositoryCloneRepository
        this.repositoryRepository = repositoryRepository
        this.server = server
        this.entityManager = entityManager
    }

    public RepositoryClone create() {
        User user = userRepositoryFixtures.createFoundRequestedUserEntity()
        Repository repository = createRandomEntityUsingUserEntity user
        return createUsingRepositoryEntity(repository)
    }

    public RepositoryClone createWithUpdatedAndCommitsStatisticsUntil(Date updated, Date commitsStatisticsUntil) {
        RepositoryClone repositoryClone = create()
        repositoryClone.setUpdated updated
        repositoryClone.setCommitsStatisticsUntil commitsStatisticsUntil
        repositoryClone.setServerId server.getServerId()
        update repositoryClone
        return repositoryClone
    }

    public Repository createRandomEntityUsingUserEntity(User userEntity) {
        Repository repositoryEntity = new Repository()
        repositoryEntity.setUser userEntity

        return repositoryRepository.save(repositoryEntity)
    }

    private RepositoryClone createUsingRepositoryEntity(Repository repository) {
        RepositoryClone repositoryClone = new RepositoryClone()
        repositoryClone.setRepository repository
        return repositoryCloneRepository.save(repositoryClone)
    }

    private void update(RepositoryClone repositoryClone) {
        repositoryCloneRepository.save repositoryClone
    }

    public void delete(RepositoryClone repositoryClone) {
        userRepositoryFixtures.deleteUserEntity repositoryClone.getRepository().getUser()
    }

}
