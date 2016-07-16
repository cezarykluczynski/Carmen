package com.cezarykluczynski.carmen.cron.management.service;

import com.cezarykluczynski.carmen.configuration.AdminPanelConfiguration;
import com.cezarykluczynski.carmen.cron.management.converter.DatabaseSwitchableJobDTOConverter;
import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO;
import com.cezarykluczynski.carmen.dao.pub.CronsDAO;
import com.cezarykluczynski.carmen.model.pub.Cron;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseSwitchableJobsService {

    private static final List<String> databaseControllableComplexJobs = Lists.newArrayList(
            AdminPanelConfiguration.USERS_IMPORT_TASK,
            AdminPanelConfiguration.REPOSITORIES_IMPORT_TASK,
            AdminPanelConfiguration.LANGUAGES_SCHEMA_UPDATE_TASK
    );

    private CronsDAO cronsDAO;

    private DatabaseSwitchableJobListProvider databaseSwitchableJobListProvider;

    @Autowired
    public DatabaseSwitchableJobsService(CronsDAO cronsDAO,
        DatabaseSwitchableJobListProvider databaseSwitchableJobListProvider) {
        this.cronsDAO = cronsDAO;
        this.databaseSwitchableJobListProvider = databaseSwitchableJobListProvider;
    }

    public List<DatabaseSwitchableJobDTO> getAll() {
        return cronsDAO.findAll().stream()
                .filter(this::isSimpleDatabaseSwitchable)
                .map(DatabaseSwitchableJobDTOConverter::fromEntity)
                .collect(Collectors.toList());
    }

    public void updateList() {
        List<String> classNames = getClassNames();
        List<String> cronNames = getCronNames();

        getCronNamesToCreate(classNames, cronNames).forEach(this::createCron);
        getCronNamesToRemove(classNames, cronNames).forEach(this::removeCron);
    }

    public void enable(DatabaseSwitchableJobDTO dto) {
        setCronStatus(dto, true);
    }


    public void disable(DatabaseSwitchableJobDTO dto) {
        setCronStatus(dto, false);
    }

    private boolean isSimpleDatabaseSwitchable(Cron cron) {
        return databaseSwitchableJobListProvider.getDatabaseSwitchableJobsClasses().stream()
                .map(Class::getSimpleName)
                .filter(className -> className.equals(cron.getName()))
                .findFirst().isPresent();
    }

    private Cron findCronByName(String name) {
        return cronsDAO.findAll().stream()
                .filter(this::isSimpleDatabaseSwitchable)
                .filter(cron -> cron.getName().equals(name))
                .findFirst().orElse(null);
    }

    private static boolean isNotDatabaseControllableComplexJob(Cron cron) {
        return !databaseControllableComplexJobs.contains(cron.getName());
    }

    private void setCronStatus(DatabaseSwitchableJobDTO dto, boolean enabled) {
        Cron cron = findCronByName(dto.getName());
        if (cron == null) {
            return;
        }

        cron.setEnabled(enabled);
        cronsDAO.update(cron);
    }

    private List<String> getClassNames() {
        return databaseSwitchableJobListProvider.getDatabaseSwitchableJobsClasses().stream()
                .map(Class::getSimpleName)
                .collect(Collectors.toList());
    }

    private List<String> getCronNames() {
        return cronsDAO.findAll().stream()
                .filter(DatabaseSwitchableJobsService::isNotDatabaseControllableComplexJob)
                .map(Cron::getName)
                .collect(Collectors.toList());
    }

    private List<String> getCronNamesToCreate(List<String> classNames, List<String> cronNames) {
        List<String> cronNamesToCreate = Lists.newArrayList();

        classNames.forEach(className -> {
            if (!cronNames.contains(className)) {
                cronNamesToCreate.add(className);
            }
        });

        return cronNamesToCreate;
    }

    private List<String> getCronNamesToRemove(List<String> classNames, List<String> cronNames) {
        List<String> cronNamesToRemove = Lists.newArrayList();

        cronNames.forEach(cronName -> {
            if (!classNames.contains(cronName)) {
                cronNamesToRemove.add(cronName);
            }
        });

        return cronNamesToRemove;
    }

    private void createCron(String cronName) {
        Cron cron = new Cron();
        cron.setName(cronName);
        cronsDAO.create(cron);
    }

    private void removeCron(String cronName) {
        cronsDAO.delete(cronsDAO.findByName(cronName).get(0));
    }

}
