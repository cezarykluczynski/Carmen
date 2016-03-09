package com.cezarykluczynski.carmen.dao.propagations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import com.cezarykluczynski.carmen.model.propagations.Repositories;
import com.cezarykluczynski.carmen.model.github.User;

@Repository("propagationsRepositoriesDAOImpl")
public class RepositoriesDAOImpl extends CarmenPropagationsDAOImpl implements RepositoriesDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositoriesDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public Repositories findByUser(User userEntity) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Repositories.class);
        criteria.add(Expression.eq("user", userEntity));
        List<Repositories> list = criteria.list();
        session.close();
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public Repositories findOldestPropagationInSleepPhase() {
        return (Repositories) findOldestPropagationInPhase(sessionFactory, Repositories.class, "sleep");
    }

    @Override
    @Transactional
    public Repositories create(User userEntity) {
        Session session = sessionFactory.openSession();

        Repositories repositoriesEntity = new Repositories();

        repositoriesEntity.setUser(userEntity);
        repositoriesEntity.setPhase("refresh");
        repositoriesEntity.setUpdated(new Date());

        session.save(repositoriesEntity);
        session.flush();
        session.close();

        return repositoriesEntity;
    }

    @Override
    @Transactional
    public void update(Repositories repositories) {
        Session session = sessionFactory.openSession();
        session.update(repositories);
        session.flush();
        session.close();
    }

    @Override
    @Transactional
    public void delete(Repositories userFollowers) {
        Session session = sessionFactory.openSession();
        session.delete(userFollowers);
        session.flush();
        session.close();
    }

    @Override
    @Transactional(readOnly = true)
    public Repositories findById(Long userId) {
        Session session = sessionFactory.openSession();
        Repositories user = (Repositories) session.get(Repositories.class, userId);
        session.close();
        return user;
    }

    @Override
    public void moveToRefreshPhase(Repositories repositoriesEntity) {
        repositoriesEntity.setUpdated(new Date());
        repositoriesEntity.setPhase("refresh");
        update(repositoriesEntity);
    }

    @Override
    public void moveToSleepPhaseUsingUserEntity(User userEntity) {
        Repositories repositoriesEntity = findByUser(userEntity);
        repositoriesEntity.setPhase("sleep");
        update(repositoriesEntity);
    }

}
