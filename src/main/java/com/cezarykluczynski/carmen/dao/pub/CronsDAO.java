package com.cezarykluczynski.carmen.dao.pub;

import com.cezarykluczynski.carmen.model.pub.Cron;

import java.util.List;

public interface CronsDAO {

    List<Cron> findByName(String name);

    Cron findByNameAndServer(String name, String server);

    Cron create(Cron cron);

    void update(Cron cron);

    void delete(Cron cron);

}
