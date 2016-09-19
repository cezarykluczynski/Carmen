package com.cezarykluczynski.carmen.cron;

import com.cezarykluczynski.carmen.cron.model.repository.CronRepository;
import com.cezarykluczynski.carmen.cron.model.entity.Cron;
import com.cezarykluczynski.carmen.util.DateUtil;
import com.google.common.base.Preconditions;

import java.time.LocalDateTime;

public class DatabaseManageableTask {

    private CronRepository cronRepository;

    private String name;

    private String server = null;

    public DatabaseManageableTask(CronRepository cronRepository, String name) {
        Preconditions.checkNotNull(cronRepository);
        Preconditions.checkNotNull(name);
        this.cronRepository = cronRepository;
        this.name = name;
    }

    public DatabaseManageableTask(CronRepository cronRepository, String name, String server) {
        this(cronRepository, name);
        Preconditions.checkNotNull(server);
        this.server = server;
    }

    public void enable() {
        Cron cron = findEntity();
        cron.setEnabled(true);
        cronRepository.save(cron);
    }

    public void disable() {
        Cron cron = findEntity();
        cron.setEnabled(false);
        cronRepository.save(cron);
    }

    public boolean isOff() {
        Cron cron = findEntity();
        return !cron.isEnabled() && !cron.isRunning();
    }

    public boolean isEnabled() {
        return findEntity().isEnabled();
    }

    public boolean isRunning() {
        return findEntity().isRunning();
    }

    public LocalDateTime getUpdated() {
        return DateUtil.toLocalDateTime(findEntity().getUpdated());
    }

    private Cron findEntity() {
        return server != null ? cronRepository.findFirstByNameAndServer(name, server) :
                cronRepository.findFirstByName(name);
    }
}
