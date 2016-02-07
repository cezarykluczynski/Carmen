package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Extend "pending_requests" table in "api_queue" schema by "params" and "propagation_id" columns.
 */
public class V9__Extend_Api_queue_pending_requests implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "ALTER TABLE api_queue.pending_requests ADD COLUMN params text;" +
            "ALTER TABLE api_queue.pending_requests ADD COLUMN propagation_id integer;"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
