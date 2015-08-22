package com.cezarykluczynski.carmen.dao.propagations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.hibernate.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.model.github.User;

@Repository
public class UserFollowersDAOImpl implements UserFollowersDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserFollowers> findByUser(User userEntity) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(UserFollowers.class);
        criteria.add(Expression.eq("user", userEntity));
        List<UserFollowers> list = criteria.list();
        session.close();
        return list;
    }

    @Transactional(readOnly = true)
    public UserFollowers findOldestPropagationInDiscoverPhase() {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(UserFollowers.class)
            .add(Restrictions.eq("phase", "discover"))
            .addOrder(Order.asc("updated"))
            .setMaxResults(1);

        UserFollowers entity = null;

        try {
            List<UserFollowers> list = criteria.list();
            entity = list.size() > 0 ? list.get(0) : null;
        } catch(ObjectNotFoundException e) {
        } finally {
            session.close();
        }

        return entity;
    }

    @Override
    @Transactional
    public UserFollowers create(User userEntity, String phase) {
        Session session = sessionFactory.openSession();

        UserFollowers userFollowersEntity = new UserFollowers();

        userFollowersEntity.setUser(userEntity);
        userFollowersEntity.setPhase(phase);
        userFollowersEntity.setUpdated();

        session.save(userFollowersEntity);
        session.flush();
        session.close();

        return userFollowersEntity;
    }

    @Override
    @Transactional
    public void update(UserFollowers userFollowers) {
        Session session = sessionFactory.openSession();
        session.update(userFollowers);
        session.flush();
        session.close();
    }

    @Override
    @Transactional(readOnly = true)
    public UserFollowers findById(Long userId) {
        Session session = sessionFactory.openSession();
        UserFollowers user = null;

        try {
            user = (UserFollowers) session.get(UserFollowers.class, userId);
        } catch(Exception e) {}

        session.close();
        return user;
    }

}
