package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.Repository;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.util.DateUtil;

import com.jcabi.github.Repo;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Repository
public class RepositoriesDAOImpl implements RepositoriesDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Repository> findByUser(User userEntity) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Repository.class);
        criteria.add(Expression.eq("user", userEntity));
        List<Repository> list = criteria.list();
        session.close();
        return list;
    }

    @Override
    public void refresh(User userEntity, List<com.cezarykluczynski.carmen.set.github.Repository> repositoriesSetList) {
        List<Repository> repositoriesListExisting = findByUser(userEntity);
        RepositoriesDAOImplListRefresherDelegate repositoriesDAOImplListRefresher =
            new RepositoriesDAOImplListRefresherDelegate(userEntity, repositoriesSetList, repositoriesListExisting);

        repositoriesDAOImplListRefresher.setSessionFactory(sessionFactory);
        repositoriesDAOImplListRefresher.refresh();
    }

    @Override
    @Transactional(readOnly = true)
    public Repository findNotForkedRepositoryWithoutClone() {
        Session session = sessionFactory.openSession();
        List<Repository> list = session.createQuery(
            "SELECT r FROM github.Repository r " +
            "LEFT JOIN r.repositoryClone rc " +
            "WHERE rc.id IS NULL AND r.fork = false"
        )
            .setMaxResults(1)
            .list();
        session.close();
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    @Transactional
    public Repository create(Repository repositoryEntity) {
        Session session = sessionFactory.openSession();
        session.save(repositoryEntity);
        session.flush();
        session.close();
        return repositoryEntity;
    }

    @Override
    @Transactional
    public void delete(Repository repositoryEntity) {
        Session session = sessionFactory.openSession();
        session.delete(repositoryEntity);
        session.flush();
        session.close();
    }

}
