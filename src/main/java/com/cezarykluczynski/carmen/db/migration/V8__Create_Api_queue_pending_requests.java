package com.cezarykluczynski.carmen.db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "pending_requests" table in "api_queue" schema.
 */
public class V8__Create_Api_queue_pending_requests implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE SCHEMA api_queue;" +
            "CREATE table api_queue.pending_requests(" +
            "    id serial," +
            "    executor character varying(255)," +
            "    path_params text," +
            "    query_params text," +
            "    priority integer NOT NULL DEFAULT 0," +
            "    github_user_id integer references github.users(id) ON DELETE CASCADE," +
            "    updated timestamp without time zone," +
            "    CONSTRAINT api_queue_pending_requests_pkey PRIMARY KEY(id)" +
            ");"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
