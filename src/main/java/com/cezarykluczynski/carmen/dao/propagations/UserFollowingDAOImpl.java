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

import com.cezarykluczynski.carmen.model.propagations.UserFollowing;
import com.cezarykluczynski.carmen.model.github.User;

@Repository
public class UserFollowingDAOImpl implements UserFollowingDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserFollowing> findByUser(User userEntity) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(UserFollowing.class);
        criteria.add(Expression.eq("user", userEntity));
        List<UserFollowing> list = criteria.list();
        session.close();
        return list;
    }

    @Transactional(readOnly = true)
    public UserFollowing findOldestPropagationInDiscoverPhase() {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(UserFollowing.class)
            .add(Restrictions.eq("phase", "discover"))
            .addOrder(Order.asc("updated"))
            .setMaxResults(1);

        UserFollowing entity = null;

        try {
            List<UserFollowing> list = criteria.list();
            entity = list.size() > 0 ? list.get(0) : null;
        } catch(ObjectNotFoundException e) {
        } finally {
            session.close();
        }

        return entity;
    }

    @Override
    @Transactional
    public UserFollowing create(User userEntity, String phase) {
        Session session = sessionFactory.openSession();

        UserFollowing userFollowingEntity = new UserFollowing();

        userFollowingEntity.setUser(userEntity);
        userFollowingEntity.setPhase(phase);
        userFollowingEntity.setUpdated();

        session.save(userFollowingEntity);
        session.flush();
        session.close();

        return userFollowingEntity;
    }

    @Override
    @Transactional
    public void update(UserFollowing userFollowing) {
        Session session = sessionFactory.openSession();
        session.update(userFollowing);
        session.flush();
        session.close();
    }

    @Override
    @Transactional(readOnly = true)
    public UserFollowing findById(Long userId) {
        Session session = sessionFactory.openSession();
        UserFollowing user = null;

        try {
            user = (UserFollowing) session.get(UserFollowing.class, userId);
        } catch(Exception e) {
        }

        session.close();
        return user;
    }

}
