package com.cezarykluczynski.carmen.db.migration.postgresql;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class V14__Add_Github_user_delete_cascades_on_propagations implements JdbcMigration {
    @SuppressWarnings("checkstyle:methodlength")
    public void migrate(Connection connection) throws Exception {
        PreparedStatement statement = connection.prepareStatement(
            "ALTER TABLE propagations.abstract_base " +
            "    DROP CONSTRAINT abstract_base_github_user_id_fkey," +
            "    ADD CONSTRAINT propagations_abstract_base_github_user_id_fkey" +
            "    FOREIGN KEY (github_user_id)" +
            "    REFERENCES github.users (id)" +
            "    MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;" +
            "ALTER TABLE propagations.repositories " +
            "    ADD CONSTRAINT propagations_repositories_github_user_id_fkey" +
            "    FOREIGN KEY (github_user_id)" +
            "    REFERENCES github.users (id)" +
            "    MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;" +
            "ALTER TABLE propagations.user_followers " +
            "    ADD CONSTRAINT propagations_user_followers_github_user_id_fkey" +
            "    FOREIGN KEY (github_user_id)" +
            "    REFERENCES github.users (id)" +
            "    MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;" +
            "ALTER TABLE propagations.user_following " +
            "    ADD CONSTRAINT propagations_user_following_github_user_id_fkey" +
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
