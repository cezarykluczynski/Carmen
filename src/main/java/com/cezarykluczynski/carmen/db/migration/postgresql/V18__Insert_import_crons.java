package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class V18__Insert_import_crons implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO public.crons(name) VALUES ('UsersImport'), ('RepositoriesImport');"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
