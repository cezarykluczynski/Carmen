package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class V21__Make_crons_name_longer implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
                "ALTER TABLE public.crons ALTER COLUMN name TYPE varchar(64);"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
