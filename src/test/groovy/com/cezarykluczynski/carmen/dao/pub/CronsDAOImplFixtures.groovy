package com.cezarykluczynski.carmen.dao.pub

import com.cezarykluczynski.carmen.model.pub.Cron

class CronsDAOImplFixtures {

    private CronsDAO cronsDAOImpl

    public CronsDAOImplFixtures(CronsDAO cronsDAOImpl) {
        this.cronsDAOImpl = cronsDAOImpl
    }

    Cron createEntityWithNameAndServer(String name, String server) {
        Cron cron = new Cron()
        cron.setName name
        cron.setServer server
        cronsDAOImpl.create cron
        return cron
    }

    void deleteEntity(Cron cron) {
        this.cronsDAOImpl.delete cron
    }

}
