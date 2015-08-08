package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "followers" table in "propagations" schema.
 */
public class V6__Create_Propagations_followers implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE table propagations.followers(" +
            ") INHERITS (propagations.abstract_base);"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
