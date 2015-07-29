package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Create "users" table in "github" schema.
 */
public class V2__Create_Github_users implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "CREATE SCHEMA github;" +
            "CREATE table github.users(" +
            "    id serial," +
            "    user_id integer REFERENCES users.users," +
            "    github_id integer," +
            "    username character varying(64)," +
            "    last_check timestamp without time zone," +
            "    found boolean default 'no'," +
            "    CONSTRAINT github_users_pkey PRIMARY KEY(id)" +
            ");"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
