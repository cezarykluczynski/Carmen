package carmen.dao.propagations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import carmen.model.propagations.UserFollowing;
import carmen.model.github.User;

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

}
