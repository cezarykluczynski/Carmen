package com.cezarykluczynski.carmen.dao.github.user;

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
public class FollowersFolloweesLinker {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public FollowersFolloweesLinker() {
    }

    @Transactional
    public void linkFollowerWithFollowee(User follower, User followee) {
        String tableName = getTableNameForLinkedUserEntities();
        String followersColumn = getKeyColumnNameForLinkedUserEntities("followers");
        String followeesColumn = getKeyColumnNameForLinkedUserEntities("followees");
        Long followerId = follower.getId();
        Long followeeId = followee.getId();
        doLinkFollowerWithFollowee(tableName, followersColumn, followeesColumn, followerId, followeeId);
    }

    private void doLinkFollowerWithFollowee(
        String tableName, String followersColumn, String followeesColumn, Long followerId, Long followeeId
    ) {
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
        BasicCollectionPersister collectionPersister =
            (BasicCollectionPersister) collectionMetadata.get("com.cezarykluczynski.carmen.model.github.User." + relationName);
        return collectionPersister;
    }

}
