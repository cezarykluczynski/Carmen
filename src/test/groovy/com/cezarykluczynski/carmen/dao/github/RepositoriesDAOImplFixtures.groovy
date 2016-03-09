package com.cezarykluczynski.carmen.dao.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.User

class RepositoriesDAOImplFixtures {

    private RepositoriesDAO githubRepositoriesDAOImpl

    private SessionFactory sessionFactory

    public RepositoriesDAOImplFixtures(RepositoriesDAO githubRepositoriesDAOImpl, SessionFactory sessionFactory) {
        this.githubRepositoriesDAOImpl = githubRepositoriesDAOImpl
        this.sessionFactory = sessionFactory
    }

    public Repository createRandomEntityUsingUserEntity(User userEntity) {
        Repository repositoryEntity = new Repository()
        repositoryEntity.setUser userEntity

        Session session = sessionFactory.openSession()
        session.save repositoryEntity
        session.flush()
        session.close()

        return repositoryEntity
    }

    public void deleteRepositoryEntity(Repository repositoryEntity) {
        githubRepositoriesDAOImpl.delete repositoryEntity
    }

}
