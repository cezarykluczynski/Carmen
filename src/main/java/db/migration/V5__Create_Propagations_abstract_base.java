package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "abstract_base" table in "propagations" schema.
 */
public class V5__Create_Propagations_abstract_base implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE SCHEMA propagations;" +
            "CREATE table propagations.abstract_base(" +
            "    id serial," +
            "    github_id integer references github.users(github_id)," +
            "    phase character varying(64)," +
            "    page integer," +
            "    updated timestamp without time zone," +
            "    CONSTRAINT propagations_abstract_base_pkey PRIMARY KEY(id)" +
            ");"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
