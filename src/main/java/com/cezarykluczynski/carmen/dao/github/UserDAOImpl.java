package com.cezarykluczynski.carmen.dao.github;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.client.github.GithubClient;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.math.BigInteger;

@Repository("githubUserDAOImpl")
public class UserDAOImpl implements UserDAO {

    private SessionFactory sessionFactory;

    private GithubClient githubClient;

    private UserDAOImplFollowersFolloweesLinkerDelegate githubUserDAOImplFollowersFolloweesLinkerDelegate;

    private UserDAOImplUserEntityHydrator userHydrator = new UserDAOImplUserEntityHydrator();

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory, GithubClient githubClient,
                       UserDAOImplFollowersFolloweesLinkerDelegate githubUserDAOImplFollowersFolloweesLinkerDelegate) {
        this.sessionFactory = sessionFactory;
        this.githubClient = githubClient;
        this.githubUserDAOImplFollowersFolloweesLinkerDelegate = githubUserDAOImplFollowersFolloweesLinkerDelegate;
    }

    @Override
    public User create(com.cezarykluczynski.carmen.set.github.User userSet) {
        User userEntity = new User();
        userEntity = hydrate(userEntity, userSet);
        create(userEntity);
        return userEntity;
    }

    @Override
    @Transactional
    public User create(User userEntity) {
        Session session = sessionFactory.openSession();
        session.save(userEntity);
        session.flush();
        session.close();
        return userEntity;
    }

    @Override
    public User update(User userEntity, com.cezarykluczynski.carmen.set.github.User userSet) {
        userEntity = hydrate(userEntity, userSet);
        update(userEntity);
        return userEntity;
    }

    @Override
    @Transactional
    public User update(User userEntity) {
        Session session = sessionFactory.openSession();
        session.update(userEntity);
        session.flush();
        session.close();
        return userEntity;
    }

    @Override
    @Transactional
    public void delete(User userEntity) {
        Session session = sessionFactory.openSession();
        session.delete(userEntity);
        session.flush();
        session.close();
    }

    @Override
    public void linkFollowerWithFollowee(User follower, User followee) {
        githubUserDAOImplFollowersFolloweesLinkerDelegate.linkFollowerWithFollowee(follower, followee);
    }

    @Override
    @Transactional
    public User createOrUpdateRequestedEntity(String login) throws IOException {
        Map flags = new HashMap<String, Boolean>();
        flags.put("requested", true);
        flags.put("optOut", false);
        return createOrUpdate(login, flags);
    }

    @Override
    @Transactional
    public User createOrUpdateGhostEntity(String login) throws IOException {
        Map flags = new HashMap<String, Boolean>();
        flags.put("requested", false);
        flags.put("optOut", false);
        return createOrUpdate(login, flags);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Expression.eq("login", login));
        List<User> list = criteria.list();
        session.close();
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public User findById(Integer userId) {
        return findById(new Long(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        Session session = sessionFactory.openSession();
        User user = (User) session.get(User.class, userId);
        session.close();
        return user;
    }

    @Override
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

    @Override
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

    @Override
    public Integer countFollowers(User user) {
        Session session = sessionFactory.openSession();

        Integer count = ((BigInteger) session.createSQLQuery(
            "SELECT count(*) FROM github.user_followers WHERE followee_id = :followeeId"
        )
            .setParameter("followeeId", user.getId())
            .uniqueResult()).intValue();
        session.close();
        return count;
    }

    @Override
    public Integer countFollowees(User user) {
        Session session = sessionFactory.openSession();

        Integer count = ((BigInteger) session.createSQLQuery(
            "SELECT count(*) FROM github.user_followers WHERE follower_id = :followerId"
        )
            .setParameter("followerId", user.getId())
            .uniqueResult()).intValue();
        session.close();
        return count;
    }

    @Override
    public Integer countFollowersFollowing(User user) {
        Session session = sessionFactory.openSession();

        Integer count = ((BigInteger) session.createSQLQuery(
            "SELECT count(*) FROM github.user_followers f1 " +
            "INNER JOIN github.user_followers f2 ON f2.follower_id = f1.followee_id " +
            "WHERE f1.follower_id = :userId AND f2.followee_id = :userId"
        )
            .setParameter("userId", user.getId())
            .uniqueResult()).intValue();

        session.close();

        return count;
    }

    @Override
    public Integer findHighestGitHubUserId() {
        Session session = sessionFactory.openSession();

        Integer highestGitHubUserId = (Integer) session.createSQLQuery(
                "SELECT u.github_id FROM github.users u ORDER BY u.github_id DESC"
        ).setMaxResults(1).uniqueResult();

        session.close();

        return highestGitHubUserId;
    }

    private User hydrate(User userEntity, com.cezarykluczynski.carmen.set.github.User userSet) {
        return userHydrator.hydrate(userEntity, userSet);
    }

    private User createOrUpdate(String login, Map<String, Boolean> flags) throws IOException {
        try {
            User userEntity = findByLogin(login);
            Boolean requested = flags.get("requested");

            if (userEntity.canBeUpdated() || requested && !userEntity.isRequested()) {
                com.cezarykluczynski.carmen.set.github.User userSet = githubClient.getUser(login);
                applyFlagsToSet(userSet, flags);
                return update(userEntity, userSet);
            }

            return userEntity;
        } catch (NullPointerException e) {
            com.cezarykluczynski.carmen.set.github.User userSet = githubClient.getUser(login);
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

}
