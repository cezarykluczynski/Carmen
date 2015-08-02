package carmen.dao.github;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import carmen.model.github.User;
import carmen.provider.github.GithubProvider;

import java.util.List;
import java.io.IOException;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    GithubProvider githubProvider;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public User create(carmen.set.github.User userSet) {
        Session session = sessionFactory.openSession();

        User userEntity = new User();

        userEntity = hydrate(userEntity, userSet);

        session.save(userEntity);
        session.flush();

        return userEntity;
    }

    @Transactional
    public User update(User userEntity, carmen.set.github.User userSet) {
        Session session = sessionFactory.openSession();

        userEntity = hydrate(userEntity, userSet);

        session.update(userEntity);
        session.flush();

        return userEntity;
    }

    public User hydrate(User userEntity, carmen.set.github.User userSet) {
        userEntity.setLogin(userSet.getLogin());
        userEntity.setFound(userSet.exists());
        userEntity.setUpdated();

        if (userEntity.getFound()) {
            userEntity.setGithubId(userSet.getId());
            userEntity.setName(userSet.getName());
        } else {
            userEntity.setGithubId();
            userEntity.setName("");
        }

        return userEntity;
    }

    @Transactional
    public User findByLogin(String login) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Expression.eq("login", login));
        List<User> list = criteria.list();
        return list.size() > 0 ? list.get(0) : null;
    }

    @Transactional
    public User createOrUpdate(String login) throws IOException {
        try {
            User userEntity = findByLogin(login);

            if (userEntity.canBeUpdated()) {
                carmen.set.github.User userSet = githubProvider.getUser(login);
                return update(userEntity, userSet);
            }

            return userEntity;
        } catch (NullPointerException e) {
            carmen.set.github.User userSet = githubProvider.getUser(login);
            return create(userSet);
        }
    }

    @Transactional
    public Object countFound() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("found", true));
        criteria.setProjection(Projections.rowCount());
        return criteria.uniqueResult();
    }
}