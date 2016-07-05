package com.cezarykluczynski.carmen.cron.management.iterator;

import org.springframework.stereotype.Service;

@Service
public class DatabaseSwitchableJobIteratorFactory {

    public DatabaseSwitchableJobsIterator createDatabaseSwitchableJobsIterator() {
        return new DatabaseSwitchableJobsIterator();
    }

}
