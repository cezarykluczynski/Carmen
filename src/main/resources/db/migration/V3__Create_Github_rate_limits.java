package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "rate_limits" table in "github" schema.
 */
public class V3__Create_Github_rate_limits implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        /**
         * Columns "resource_limit" and "resource_reset" are named so because of "reset" and "limit"
         * being PostgreSQL keywords.
         *
         * @see http://www.postgresql.org/docs/9.0/static/sql-keywords-appendix.html
         */
        PreparedStatement statement = connection.prepareStatement(
            "CREATE table github.rate_limits (" +
            "    id serial," +
            "    resource character varying(16)," +
            "    resource_limit integer NOT NULL," +
            "    remaining integer NOT NULL," +
            "    updated timestamp without time zone," +
            "    resource_reset timestamp without time zone," +
            "    CONSTRAINT github_rate_limits_pkey PRIMARY KEY(id)" +
            ");"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
