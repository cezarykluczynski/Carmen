package com.cezarykluczynski.carmen.cron.management.service;

import com.cezarykluczynski.carmen.cron.management.iterator.DatabaseSwitchableJobsIterator;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseSwitchableJobListProvider {

    private final List<Class> databaseSwitchableJobsClasses = Lists.newArrayList();

    public DatabaseSwitchableJobListProvider() {
        DatabaseSwitchableJobsIterator iterator = createIterator();

        while(iterator.hasNext()) {
            databaseSwitchableJobsClasses.add(iterator.next());
        }
    }

    List<Class> getDatabaseSwitchableJobsClasses() {
        return Lists.newArrayList(databaseSwitchableJobsClasses);
    }

    private DatabaseSwitchableJobsIterator createIterator() {
        return new DatabaseSwitchableJobsIterator();
    }

}
