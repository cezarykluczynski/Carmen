package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Extends "github.users" by all fields returned by /users/[:username] API endpoint,
 * minus statistics ("public_repos", "public_gists", "followers", "following")
 * and timestamps ("created_at", "updated_at"), that, at the point of writing this,
 * does not present any value that can't be later obtained by other measures.
 * Additionally, PostgreSQL is not planned to be used to hold statistic data.
 * This also adds "requested" and "opt_put" columns for tracking user state inside application.
 */
public class V4__Extend_Github_users implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "ALTER TABLE github.users ADD COLUMN requested boolean default 'no';" +
            "ALTER TABLE github.users ADD COLUMN opt_out boolean default 'no';" +
            "ALTER TABLE github.users ADD COLUMN avatar_url character varying(255);" +
            "ALTER TABLE github.users ADD COLUMN github_type character varying(255);" +
            "ALTER TABLE github.users ADD COLUMN site_admin boolean default 'no';" +
            "ALTER TABLE github.users ADD COLUMN company character varying(255);" +
            "ALTER TABLE github.users ADD COLUMN blog character varying(255);" +
            "ALTER TABLE github.users ADD COLUMN github_location character varying(255);" +
            "ALTER TABLE github.users ADD COLUMN email character varying(255);" +
            "ALTER TABLE github.users ADD COLUMN hireable boolean default 'no';" +
            "ALTER TABLE github.users ADD COLUMN bio text;"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
