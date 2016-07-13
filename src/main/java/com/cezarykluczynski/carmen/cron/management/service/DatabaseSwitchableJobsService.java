package com.cezarykluczynski.carmen.cron.management.service;

import com.cezarykluczynski.carmen.cron.management.converter.DatabaseSwitchableJobDTOConverter;
import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO;
import com.cezarykluczynski.carmen.dao.pub.CronsDAO;
import com.cezarykluczynski.carmen.model.pub.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseSwitchableJobsService {

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

    private void setCronStatus(DatabaseSwitchableJobDTO dto, boolean enabled) {
        Cron cron = findCronByName(dto.getName());
        if (cron == null) {
            return;
        }

        cron.setEnabled(enabled);
        cronsDAO.update(cron);
    }


}
