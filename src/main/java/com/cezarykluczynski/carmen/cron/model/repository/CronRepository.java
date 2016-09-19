package com.cezarykluczynski.carmen.cron.model.repository;

import com.cezarykluczynski.carmen.cron.model.entity.Cron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CronRepository extends JpaRepository<Cron, Long> {

    List<Cron> findAllByName(String name);

    Cron findFirstByName(String name);

    Cron findFirstByNameAndServer(String name, String server);

}
