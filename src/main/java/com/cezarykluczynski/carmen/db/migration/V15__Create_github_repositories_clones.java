package com.cezarykluczynski.carmen.db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class V15__Create_github_repositories_clones implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE table github.repositories_clones(" +
            "    id serial," +
            "    github_repository_id integer NOT NULL REFERENCES github.repositories(id) ON DELETE CASCADE," +
            "    cloned timestamp without time zone," +
            "    updated timestamp without time zone," +
            "    parent_id integer REFERENCES github.repositories_clones(id) ON DELETE CASCADE," +
            "    location_directory varchar(8)," +
            "    location_subdirectory varchar(255)," +
            "    server_id varchar(16)," +
            "    CONSTRAINT github_repositories_clones_pkey PRIMARY KEY(id)" +
            ");"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
