package com.cezarykluczynski.carmen.dao.github;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.persister.collection.BasicCollectionPersister;

import com.cezarykluczynski.carmen.model.github.User;

import java.util.Map;

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
        String tableName = getTableNameForLinkedUserEntities();
        String followersColumn = getKeyColumnNameForLinkedUserEntities("followers");
        String followeesColumn = getKeyColumnNameForLinkedUserEntities("followees");

        Session session = sessionFactory.openSession();
        try {
            session
                .createSQLQuery(
                    "INSERT INTO " + tableName + "(" +followersColumn + ", " + followeesColumn + ") " +
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

    private String getKeyColumnNameForLinkedUserEntities(String relationName) {
        BasicCollectionPersister collectionPersister = getLinkedCollectionPersister(relationName);
        String columnName = collectionPersister.getKeyColumnNames()[0];
        return columnName;
    }

    private String getTableNameForLinkedUserEntities() {
        BasicCollectionPersister collectionPersister = getLinkedCollectionPersister("followers");
        String tableName = collectionPersister.getTableName();
        return tableName;
    }

    private BasicCollectionPersister getLinkedCollectionPersister(String relationName) {
        Map collectionMetadata = sessionFactory.getAllCollectionMetadata();
        BasicCollectionPersister collectionPersister = (BasicCollectionPersister)
                collectionMetadata.get("com.cezarykluczynski.carmen.model.github.User." + relationName);
        return collectionPersister;
    }

}
