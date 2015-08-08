package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "following" table in "propagations" schema.
 */
public class V7__Create_Propagations_following implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE table propagations.following(" +
            ") INHERITS (propagations.abstract_base);"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
