package com.cezarykluczynski.carmen.cron;

import com.cezarykluczynski.carmen.dao.pub.CronsDAO;
import com.cezarykluczynski.carmen.model.pub.Cron;
import com.google.common.base.Preconditions;

import java.time.LocalDateTime;

public class DatabaseManageableTask {

    private CronsDAO cronsDAO;

    private String name;

    private String server = null;

    public DatabaseManageableTask(CronsDAO cronsDAO, String name) {
        Preconditions.checkNotNull(cronsDAO);
        Preconditions.checkNotNull(name);
        this.cronsDAO = cronsDAO;
        this.name = name;
    }

    public DatabaseManageableTask(CronsDAO cronsDAO, String name, String server) {
        this(cronsDAO, name);
        Preconditions.checkNotNull(server);
        this.server = server;
    }

    public void enable() {
        Cron cron = findEntity();
        cron.setEnabled(true);
        cronsDAO.update(cron);
    }

    public void disable() {
        Cron cron = findEntity();
        cron.setEnabled(false);
        cronsDAO.update(cron);
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
        return findEntity().getUpdated();
    }

    private Cron findEntity() {
        return server != null ? cronsDAO.findByNameAndServer(name, server) : cronsDAO.findByName(name).get(0);
    }
}
