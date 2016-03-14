package com.cezarykluczynski.carmen.dao.pub;

import com.cezarykluczynski.carmen.model.pub.Cron;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("publicCronsDAOImpl")
public class CronsDAOImpl implements CronsDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public CronsDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cron> findByName(String name) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Cron.class);
        criteria.add(Expression.eq("name", name));
        List<Cron> list = criteria.list();
        session.close();
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Cron findByNameAndServer(String name, String server) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Cron.class);
        criteria.add(Expression.eq("name", name));
        criteria.add(Expression.eq("server", server));
        criteria.setMaxResults(1);
        List<Cron> list = criteria.list();
        session.close();
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    @Transactional
    public Cron create(Cron cron) {
        Session session = sessionFactory.openSession();
        session.save(cron);
        session.flush();
        session.close();
        return cron;
    }

    @Override
    @Transactional
    public void update(Cron cron) {
        Session session = sessionFactory.openSession();
        session.update(cron);
        session.flush();
        session.close();
    }

    @Override
    @Transactional
    public void delete(Cron cron) {
        Session session = sessionFactory.openSession();
        session.delete(cron);
        session.flush();
        session.close();
    }

}
