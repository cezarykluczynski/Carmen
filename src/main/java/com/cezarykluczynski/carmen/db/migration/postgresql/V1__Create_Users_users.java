package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "users" table in "users" schema.
 */
public class V1__Create_Users_users implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE SCHEMA users;" +
            "CREATE table users.users(" +
            "    id serial," +
            "    CONSTRAINT users_users_pkey PRIMARY KEY(id)" +
            ");"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
