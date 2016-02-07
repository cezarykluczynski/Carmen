package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "repositories" table in "github" schema.
 */
public class V11__Create_Github_repositories implements JdbcMigration {
    @SuppressWarnings("checkstyle:methodlength")
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE table github.repositories(" +
            "    id serial," +
            "    github_id integer," +
            "    github_user_id integer references github.users(id)," +
            "    parent_id integer references github.repositories(id)," +
            "    name character varying(255)," +
            "    full_name character varying(255)," +
            "    description text," +
            "    homepage character varying(255)," +
            "    fork boolean default 'no'," +
            "    default_branch character varying(255)," +
            "    created timestamp without time zone," +
            "    updated timestamp without time zone," +
            "    pushed timestamp without time zone," +
            "    clone_url character varying(255)," +
            "    CONSTRAINT repositories_github_pkey PRIMARY KEY(id)" +
            ");"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
