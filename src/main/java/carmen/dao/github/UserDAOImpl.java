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
import java.lang.Boolean;
import java.util.Map;
import java.util.HashMap;
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
        session.close();

        return userEntity;
    }

    @Transactional
    public User update(User userEntity, carmen.set.github.User userSet) {
        Session session = sessionFactory.openSession();

        userEntity = hydrate(userEntity, userSet);

        session.update(userEntity);
        session.flush();
        session.close();

        return userEntity;
    }

    public User hydrate(User userEntity, carmen.set.github.User userSet) {
        userEntity.setLogin(userSet.getLogin());
        userEntity.setFound(userSet.exists());
        userEntity.setRequested(userSet.getRequested());
        userEntity.setOptOut(userSet.getOptOut());
        userEntity.setUpdated();

        if (userEntity.getFound()) {
            hydrateUserEntityUsingExistingUserSet(userEntity, userSet);
        } else {
            hydrateUserEntityWithNonExistingUser(userEntity);
        }

        return userEntity;
    }

    private void hydrateUserEntityUsingExistingUserSet(User userEntity, carmen.set.github.User userSet) {
        userEntity.setGithubId(userSet.getId());
        userEntity.setName(userSet.getName());
        userEntity.setAvatarUrl(userSet.getAvatarUrl());
        userEntity.setType(userSet.getType());
        userEntity.setSiteAdmin(userSet.getSiteAdmin());
        userEntity.setCompany(userSet.getCompany());
        userEntity.setBlog(userSet.getBlog());
        userEntity.setLocation(userSet.getLocation());
        userEntity.setEmail(userSet.getEmail());
        userEntity.setHireable(userSet.getHireable());
        userEntity.setBio(userSet.getBio());
    }

    private void hydrateUserEntityWithNonExistingUser(User userEntity) {
        userEntity.setGithubId();
        userEntity.setName("");
        userEntity.setAvatarUrl("");
        userEntity.setType("");
        userEntity.setSiteAdmin(false);
        userEntity.setCompany("");
        userEntity.setBlog("");
        userEntity.setLocation("");
        userEntity.setEmail("");
        userEntity.setHireable(false);
        userEntity.setBio("");
    }

    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Expression.eq("login", login));
        List<User> list = criteria.list();
        session.close();
        return list.size() > 0 ? list.get(0) : null;
    }

    @Transactional
    private User createOrUpdate(String login, Map<String, Boolean> flags) throws IOException {
        try {
            User userEntity = findByLogin(login);

            if (userEntity.canBeUpdated()) {
                carmen.set.github.User userSet = githubProvider.getUser(login);
                applyFlagsToSet(userSet, flags);
                return update(userEntity, userSet);
            }

            return userEntity;
        } catch (NullPointerException e) {
            carmen.set.github.User userSet = githubProvider.getUser(login);
            applyFlagsToSet(userSet, flags);
            return create(userSet);
        }
    }

    private void applyFlagsToSet(carmen.set.github.User userSet, Map<String, Boolean> flags) {
        if (flags.containsKey("requested")) {
            userSet.setRequested(flags.get("requested"));
        }
        if (flags.containsKey("optOut")) {
            userSet.setOptOut(flags.get("optOut"));
        }
    }

    @Transactional
    public User createOrUpdateRequestedEntity(String login) throws IOException {
        Map flags = new HashMap<String, Boolean>();
        flags.put("requested", true);
        flags.put("optOut", false);
        return createOrUpdate(login, flags);
    }

    @Transactional
    public User createOrUpdateGhostEntity(String login) throws IOException {
        Map flags = new HashMap<String, Boolean>();
        flags.put("requested", false);
        flags.put("optOut", false);
        return createOrUpdate(login, flags);
    }

    @Transactional(readOnly = true)
    public Object countFound() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("found", true));
        criteria.setProjection(Projections.rowCount());
        Object result = criteria.uniqueResult();
        session.close();
        return result;
    }
}
