package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository;

import com.cezarykluczynski.carmen.model.propagations.Propagation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.List;

class AbstractPropagationRepositoryImpl<T extends Propagation> {

    private final EntityManager entityManager;

    private final Class clazz;

    AbstractPropagationRepositoryImpl(EntityManager entityManager, Class clazz) {
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    T findOldestPropagationInPhase(String phase) {
        Criteria criteria = entityManager.unwrap(Session.class).createCriteria(clazz)
                .add(Restrictions.eq("phase", phase))
                .addOrder(Order.asc("updated"))
                .setMaxResults(1);

        List<T> list = criteria.list();
        return list.size() > 0 ? list.get(0) : null;
    }

}
