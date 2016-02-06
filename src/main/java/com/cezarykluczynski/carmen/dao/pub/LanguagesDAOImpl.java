package com.cezarykluczynski.carmen.dao.pub;

import com.cezarykluczynski.carmen.model.pub.Language;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Repository
public class LanguagesDAOImpl implements LanguagesDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
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
}
