package com.cezarykluczynski.carmen.dao.users.propagations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import com.cezarykluczynski.carmen.dao.users.CarmenRelationalDAOImpl;

import java.util.List;

public class CarmenPropagationsDAOImpl extends CarmenRelationalDAOImpl {

    protected CarmenRelationalEntity findOldestPropagationInPhase(SessionFactory sessionFactory, Class clazz, String phase) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(clazz)
            .add(Restrictions.eq("phase", phase))
            .addOrder(Order.asc("updated"))
            .setMaxResults(1);

        CarmenRelationalEntity entity = null;
        List<CarmenRelationalEntity> list = criteria.list();
        if (list.size() > 0) {
            entity = list.get(0);
        }
        session.close();

        return entity;
    }

}
