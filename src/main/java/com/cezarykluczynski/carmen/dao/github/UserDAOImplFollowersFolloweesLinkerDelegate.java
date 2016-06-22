package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserDAOImplFollowersFolloweesLinkerDelegate {

    private SessionFactory sessionFactory;

    @Autowired
    public UserDAOImplFollowersFolloweesLinkerDelegate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void linkFollowerWithFollowee(User follower, User followee) {
        doLinkFollowerWithFollowee(follower.getId(), followee.getId());
    }

    private void doLinkFollowerWithFollowee(Long followerId, Long followeeId) {
        Session session = sessionFactory.openSession();
        try {
            session
                .createSQLQuery(
                    "INSERT INTO github.user_followers(follower_id, followee_id) " +
                    "VALUES (:followerId, :followeeId)"
                )
                .setParameter("followerId", followerId)
                .setParameter("followeeId", followeeId)
                .executeUpdate();
        } catch (ConstraintViolationException e) {
            /* Constraint already exists, because it was created when this method was executed
               when current users roles were reversed. */
        } finally {
            session.close();
        }
    }

}
