package com.cezarykluczynski.carmen.db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "repositories" table in "propagations" schema.
 */
public class V12__Create_Propagations_repositories implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE table propagations.repositories(" +
            "    CONSTRAINT propagations_repositories_pkey PRIMARY KEY(id)" +
            ") INHERITS (propagations.abstract_base);"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
