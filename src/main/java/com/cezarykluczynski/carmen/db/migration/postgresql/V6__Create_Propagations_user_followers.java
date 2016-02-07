package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "followers" table in "propagations" schema.
 */
public class V6__Create_Propagations_user_followers implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE table propagations.user_followers(" +
            "    CONSTRAINT propagations_user_followers_pkey PRIMARY KEY(id)" +
            ") INHERITS (propagations.abstract_base);"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
