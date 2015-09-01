package com.cezarykluczynski.carmen.dao.github;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.collection.BasicCollectionPersister;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.provider.github.GithubProvider;

import java.util.List;
import java.lang.Boolean;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.math.BigInteger;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    GithubProvider githubProvider;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User create(com.cezarykluczynski.carmen.set.github.User userSet) {
        User userEntity = new User();
        userEntity = hydrate(userEntity, userSet);
        create(userEntity);
        return userEntity;
    }

    @Transactional
    public User create(User userEntity) {
        Session session = sessionFactory.openSession();
        session.save(userEntity);
        session.flush();
        session.close();
        return userEntity;
    }

    public User update(User userEntity, com.cezarykluczynski.carmen.set.github.User userSet) {
        userEntity = hydrate(userEntity, userSet);
        update(userEntity);
        return userEntity;
    }

    @Transactional
    public User update(User userEntity) {
        Session session = sessionFactory.openSession();
        session.update(userEntity);
        session.flush();
        session.close();
        return userEntity;
    }

    @Transactional
    public void delete(User userEntity) {
        Session session = sessionFactory.openSession();
        session.delete(userEntity);
        session.flush();
        session.close();
    }

    @Transactional
    public void linkFollowerWithFollowee(User follower, User followee) {
        Session session = sessionFactory.openSession();

        Map collectionMetadata = sessionFactory.getAllCollectionMetadata();
        BasicCollectionPersister followersCollectionPersister =
            (BasicCollectionPersister) collectionMetadata.get("com.cezarykluczynski.carmen.model.github.User.followers");
        BasicCollectionPersister followeesCollectionPersister =
            (BasicCollectionPersister) collectionMetadata.get("com.cezarykluczynski.carmen.model.github.User.followees");
        String tableName = followersCollectionPersister.getTableName();
        String followersColumn = followersCollectionPersister.getKeyColumnNames()[0];
        String followeesColumn = followeesCollectionPersister.getKeyColumnNames()[0];

        try {
            session
                .createSQLQuery(
                    "INSERT INTO " + tableName + "(" +followersColumn + ", " + followeesColumn + ") " +
                    "VALUES (:followerId, :followeeId)"
                )
                .setParameter("followerId", follower.getId())
                .setParameter("followeeId", followee.getId())
                .executeUpdate();
        } catch (org.hibernate.exception.ConstraintViolationException e) {
        } finally {
            session.close();
        }
    }

    public User hydrate(User userEntity, com.cezarykluczynski.carmen.set.github.User userSet) {
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

    private void hydrateUserEntityUsingExistingUserSet(User userEntity, com.cezarykluczynski.carmen.set.github.User userSet) {
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
            Boolean requested = flags.get("requested");
            Boolean userEntityHasBeenRequested = requested && !userEntity.getRequested();

            if (userEntity.canBeUpdated() || userEntityHasBeenRequested) {
                com.cezarykluczynski.carmen.set.github.User userSet = githubProvider.getUser(login);
                applyFlagsToSet(userSet, flags);
                return update(userEntity, userSet);
            }

            return userEntity;
        } catch (NullPointerException e) {
            com.cezarykluczynski.carmen.set.github.User userSet = githubProvider.getUser(login);
            applyFlagsToSet(userSet, flags);
            return create(userSet);
        }
    }

    private void applyFlagsToSet(com.cezarykluczynski.carmen.set.github.User userSet, Map<String, Boolean> flags) {
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

    public User findById(Integer userId) {
        return findById(new Long(userId));
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        Session session = sessionFactory.openSession();
        User user = null;

        try {
            user = (User) session.get(User.class, userId);
        } catch(Exception e) {
        }

        session.close();
        return user;
    }

    @Transactional(readOnly = true)
    public Object countFound() {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("found", true));
        criteria.add(Restrictions.eq("requested", true));
        criteria.setProjection(Projections.rowCount());
        Object result = criteria.uniqueResult();
        session.close();
        return result;
    }

    public User findUserInReportFollowersFolloweesPhase() throws IOException {
        Session session = sessionFactory.openSession();
        List<User> list = session.createQuery(
            "SELECT u FROM github.User u " +
            "LEFT JOIN u.userFollowers fs " +
            "LEFT JOIN u.userFollowing fg " +
            "WHERE fs.phase = :phase AND fg.phase = :phase " +
            "ORDER BY fs.updated ASC, fg.updated ASC"
        )
            .setString("phase", "report")
            .setMaxResults(1)
            .list();
        session.close();
       return list.size() > 0 ? list.get(0) : null;
    }

    public Integer countFollowers(User user) {
        Session session = sessionFactory.openSession();

        return ((BigInteger) session.createSQLQuery(
            "SELECT count(*) FROM github.user_followers WHERE followee_id = :followeeId"
        )
            .setParameter("followeeId", user.getId())
            .uniqueResult()).intValue();
    }

    public Integer countFollowees(User user) {
        Session session = sessionFactory.openSession();

        return ((BigInteger) session.createSQLQuery(
            "SELECT count(*) FROM github.user_followers WHERE follower_id = :followerId"
        )
            .setParameter("followerId", user.getId())
            .uniqueResult()).intValue();
    }

    public Integer countFollowersFollowing(User user) {
        Session session = sessionFactory.openSession();

        return ((BigInteger) session.createSQLQuery(
            "SELECT count(*) FROM github.user_followers f1 " +
            "INNER JOIN github.user_followers f2 ON f2.follower_id = f1.followee_id " +
            "WHERE f1.follower_id = :userId AND f2.followee_id = :userId"
        )
            .setParameter("userId", user.getId())
            .uniqueResult()).intValue();
    }

}
