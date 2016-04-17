package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class V19__Extend_Imports_table_by_updated_column implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "ALTER TABLE public.crons ADD COLUMN updated timestamp without time zone;" +
            "INSERT INTO public.crons(name) VALUES ('LanguagesSchemaUpdate');"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
