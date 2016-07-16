package com.cezarykluczynski.carmen.configuration;

import com.cezarykluczynski.carmen.cron.DatabaseManageableTask;
import com.cezarykluczynski.carmen.dao.pub.CronsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("admin-panel")
public class AdminPanelConfiguration {

    public static final String USERS_IMPORT_TASK = "UsersImport";

    public static final String REPOSITORIES_IMPORT_TASK = "RepositoriesImport";

    public static final String LANGUAGES_SCHEMA_UPDATE_TASK = "LanguagesSchemaUpdate";

    @Autowired
    ApplicationContext ctx;

    @Bean(name = "usersImportTask")
    DatabaseManageableTask usersImportTask() {
        return new DatabaseManageableTask(getCronsDAOBean(), USERS_IMPORT_TASK);
    }

    @Bean(name = "repositoriesImportTask")
    DatabaseManageableTask repositoriesImportTask() {
        return new DatabaseManageableTask(getCronsDAOBean(), REPOSITORIES_IMPORT_TASK);
    }

    @Bean(name = "languagesSchemaUpdateTask")
    DatabaseManageableTask languagesSchemaUpdateTask() {
        return new DatabaseManageableTask(getCronsDAOBean(), LANGUAGES_SCHEMA_UPDATE_TASK);
    }

    private CronsDAO getCronsDAOBean() {
        return ctx.getBean(CronsDAO.class);
    }

}
