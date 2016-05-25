package com.cezarykluczynski.carmen.dao.pub;

import com.cezarykluczynski.carmen.model.pub.Language;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Repository("publicLanguagesDAOImpl")
public class LanguagesDAOImpl implements LanguagesDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public LanguagesDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Language> findAll() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Language.class);
        List<Language> list = criteria.list();
        session.close();
        return list;

    }

    @Override
    @Transactional
    public void saveAll(List<Language> languageList) {
        Session session = sessionFactory.openSession();
        languageList.stream().forEach(session::saveOrUpdate);
        session.flush();
        session.close();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer countAll() {
        Session session = sessionFactory.openSession();
        Integer count = ((Long) session.createCriteria(Language.class)
                .setProjection(Projections.rowCount())
                .uniqueResult()).intValue();
        session.close();
        return count;
    }

}
