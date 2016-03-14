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

    @Autowired
    ApplicationContext ctx;

    @Bean(name = "usersImportTask")
    DatabaseManageableTask usersImportTask() {
        return new DatabaseManageableTask(getCronsDAOBean(), "UsersImport");
    }

    @Bean(name = "repositoriesImportTask")
    DatabaseManageableTask repositoriesImportTask() {
        return new DatabaseManageableTask(getCronsDAOBean(), "RepositoriesImport");
    }

    private CronsDAO getCronsDAOBean() {
        return ctx.getBean(CronsDAO.class);
    }

}
