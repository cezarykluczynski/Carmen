package com.cezarykluczynski.carmen.db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "user_followers" table in "github" schema.
 */
public class V10__Create_Github_user_followers implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE table github.user_followers(" +
            "    followee_id integer NOT NULL REFERENCES github.users(id)," +
            "    follower_id integer NOT NULL REFERENCES github.users(id)," +
            "    CONSTRAINT github_user_followers_joined_unique UNIQUE (followee_id, follower_id)" +
            ");"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
