package carmen.dao.users;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Object count() {
        Session session = sessionFactory.openSession();
        return session.createCriteria("carmen.model.users.User")
            .setProjection(Projections.rowCount())
            .uniqueResult();
    }
}