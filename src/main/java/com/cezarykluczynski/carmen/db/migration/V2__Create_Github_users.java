package com.cezarykluczynski.carmen.db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "users" table in "github" schema.
 */
public class V2__Create_Github_users implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE SCHEMA github;" +
            "CREATE table github.users(" +
            "    id serial," +
            "    user_id integer REFERENCES users.users," +
            "    github_id integer," +
            "    login character varying(64)," +
            "    name character varying(128)," +
            "    updated timestamp without time zone," +
            "    found boolean default 'no'," +
            "    CONSTRAINT github_users_pkey PRIMARY KEY(id)," +
            "    CONSTRAINT github_users_github_id_unique UNIQUE (github_id)," +
            "    CONSTRAINT github_users_login_unique UNIQUE (login)" +
            ");"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
