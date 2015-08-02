package carmen.dao.github;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import carmen.model.github.RateLimit;

import java.util.List;

@Repository
public class RateLimitDAOImpl implements RateLimitDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public RateLimit create(carmen.set.github.RateLimit rateLimitSet) {
        Session session = sessionFactory.openSession();

        RateLimit rateLimitEntity = new RateLimit();

        rateLimitEntity.setResource(rateLimitSet.getResource());
        rateLimitEntity.setLimit(rateLimitSet.getLimit());
        rateLimitEntity.setRemaining(rateLimitSet.getRemaining());
        rateLimitEntity.setReset(rateLimitSet.getReset());
        rateLimitEntity.setUpdated();

        session.save(rateLimitEntity);
        session.flush();
        session.close();

        return rateLimitEntity;
    }

    @Transactional(readOnly = true)
    public RateLimit getCoreLimit() {
        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(RateLimit.class)
            .add(Restrictions.eq("resource", "core"))
            .addOrder(Order.desc("reset"))
            .setMaxResults(1);

        List<RateLimit> list = criteria.list();
        session.close();
        return list.size() > 0 ? list.get(0) : null;
    }

    @Transactional
    public void decrementRateLimitRemainingCounter() {
        Session session = sessionFactory.openSession();

        session.createQuery("update github.RateLimit set remaining = remaining - 1").executeUpdate();
        session.close();
    }

    // TODO
    public RateLimit getSearchLimit() {
        return new RateLimit();
    }

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
}