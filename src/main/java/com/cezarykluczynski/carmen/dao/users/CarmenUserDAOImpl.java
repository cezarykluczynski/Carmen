package com.cezarykluczynski.carmen.dao.users;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CarmenUserDAOImpl implements CarmenUserDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public CarmenUserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public Object count() {
        Session session = sessionFactory.openSession();
        Object result = session.createCriteria("com.cezarykluczynski.carmen.model.users.User")
            .setProjection(Projections.rowCount())
            .uniqueResult();
        session.close();
        return result;
    }
}
