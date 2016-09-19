package com.cezarykluczynski.carmen.cron.model.repository

import com.cezarykluczynski.carmen.cron.model.entity.Cron

class CronRepositoryFixtures {

    private CronRepository cronRepository

    public CronRepositoryFixtures(CronRepository cronRepository) {
        this.cronRepository = cronRepository
    }

    Cron createEntityWithNameAndServer(String name, String server) {
        Cron cron = new Cron()
        cron.setName name
        cron.setServer server
        return cronRepository.save(cron)
    }

    Cron createEntityWithNameAndServer(String name) {
        Cron cron = new Cron()
        cron.setName name
        return cronRepository.save(cron)
    }

    void deleteEntity(Cron cron) {
        cronRepository.delete cron
    }

}
