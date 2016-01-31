package com.cezarykluczynski.carmen.db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class V13__Add_Github_user_delete_cascades implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "ALTER TABLE propagations.abstract_base " +
            "    DROP CONSTRAINT abstract_base_github_user_id_fkey," +
            "    ADD CONSTRAINT abstract_base_github_user_id_fkey" +
            "    FOREIGN KEY (github_user_id)" +
            "    REFERENCES github.users (id)" +
            "    MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;" +
            "ALTER TABLE github.repositories " +
            "    DROP CONSTRAINT repositories_github_user_id_fkey," +
            "    ADD CONSTRAINT repositories_github_user_id_fkey" +
            "    FOREIGN KEY (github_user_id)" +
            "    REFERENCES github.users (id)" +
            "    MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;"
        );

        try {
            statement.execute();
        } finally {
            statement.close();
        }
    }
}
