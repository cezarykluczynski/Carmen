package com.cezarykluczynski.carmen.dao.github

import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.vcs.server.Server

class RepositoriesClonesDAOImplFixtures {

    private UserDAOImplFixtures githubUserDAOImplFixtures

    private RepositoriesDAOImplFixtures repositoriesDAOImplFixtures

    private RepositoriesClonesDAO repositoriesClonesDAO

    private Server server

    public RepositoriesClonesDAOImplFixtures(UserDAOImplFixtures githubUserDAOImplFixtures,
                                             RepositoriesDAOImplFixtures repositoriesDAOImplFixtures,
                                             RepositoriesClonesDAO repositoriesClonesDAO,
                                             Server server) {
        this.githubUserDAOImplFixtures = githubUserDAOImplFixtures
        this.repositoriesDAOImplFixtures = repositoriesDAOImplFixtures
        this.repositoriesClonesDAO = repositoriesClonesDAO
        this.server = server
    }

    public RepositoryClone create() {
        User user = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repository repository = repositoriesDAOImplFixtures.createRandomEntityUsingUserEntity user
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

    private RepositoryClone createUsingRepositoryEntity(Repository repository) {
        RepositoryClone repositoryClone = new RepositoryClone()
        repositoryClone.setRepository repository
        repositoriesClonesDAO.create repositoryClone
        return repositoryClone
    }

    private void update(RepositoryClone repositoryClone) {
        repositoriesClonesDAO.update repositoryClone
    }

    public void delete(RepositoryClone repositoryClone) {
        githubUserDAOImplFixtures.deleteUserEntity repositoryClone.getRepository().getUser()
    }

}
