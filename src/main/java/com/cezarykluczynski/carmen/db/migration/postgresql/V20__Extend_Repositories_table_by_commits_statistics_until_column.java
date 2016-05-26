package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class V20__Extend_Repositories_table_by_commits_statistics_until_column implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "ALTER TABLE github.repositories ADD COLUMN commits_statistics_until timestamp without time zone;"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
