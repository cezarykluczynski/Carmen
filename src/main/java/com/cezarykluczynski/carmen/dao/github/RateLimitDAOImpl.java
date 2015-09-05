package com.cezarykluczynski.carmen.dao.github;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cezarykluczynski.carmen.model.github.RateLimit;

import java.util.List;

@Repository
public class RateLimitDAOImpl implements RateLimitDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public RateLimit create(com.cezarykluczynski.carmen.set.github.RateLimit rateLimitSet) {
        RateLimit rateLimitEntity = new RateLimit();
        rateLimitEntity.setResource(rateLimitSet.getResource());
        rateLimitEntity.setLimit(rateLimitSet.getLimit());
        rateLimitEntity.setRemaining(rateLimitSet.getRemaining());
        rateLimitEntity.setReset(rateLimitSet.getReset());
        rateLimitEntity.setUpdated();

        Session session = sessionFactory.openSession();
        session.save(rateLimitEntity);
        session.flush();
        session.close();

        return rateLimitEntity;
    }

    @Override
    public RateLimit getCoreLimit() {
        return getLimitByResource("core");
    }

    @Override
    public RateLimit getSearchLimit() {
        return getLimitByResource("search");
    }

    @Override
    @Transactional
    public void decrementRateLimitRemainingCounter() {
        Session session = sessionFactory.openSession();

        session.createQuery("update github.RateLimit set remaining = remaining - 1").executeUpdate();
        session.close();
    }

    @Override
    @Transactional
    public void deleteOldLimits(String resource) {
        Session session = sessionFactory.openSession();

        RateLimit currentLimit = getCoreLimit();

        session.createQuery("delete from github.RateLimit r where resource = :resource and r.id != :currentLimitId")
            .setString("resource", resource)
            .setLong("currentLimitId", currentLimit.getId())
            .executeUpdate();
        session.close();
    }

    @Override
    @Transactional
    public void delete(RateLimit rateLimitEntity) {
        Session session = sessionFactory.openSession();
        session.delete(rateLimitEntity);
        session.flush();
        session.close();
    }

    @Transactional(readOnly = true)
    private RateLimit getLimitByResource(String resource) {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(RateLimit.class)
            .add(Restrictions.eq("resource", resource))
            .addOrder(Order.desc("reset"))
            .setMaxResults(1);

        List<RateLimit> list = criteria.list();
        session.close();
        return list.get(0);
    }

}
