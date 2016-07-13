package com.cezarykluczynski.carmen.cron.management.iterator;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import com.cezarykluczynski.carmen.util.iterator.AnnotatedClassesIterator;

public class DatabaseSwitchableJobsIterator extends AnnotatedClassesIterator {

    public DatabaseSwitchableJobsIterator() {
        super(DatabaseSwitchableJob.class);
    }

}
