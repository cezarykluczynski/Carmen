package com.cezarykluczynski.carmen.dao.github

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import org.hibernate.Session
import org.hibernate.SessionFactory

import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAO

@Component
class RepositoriesDAOImplFixtures {

    @Autowired
    RepositoriesDAO githubRepositoriesDAOImpl

    @Autowired
    private SessionFactory sessionFactory

    public void setSessionFactory(SessionFactory sessionFactory) {
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
