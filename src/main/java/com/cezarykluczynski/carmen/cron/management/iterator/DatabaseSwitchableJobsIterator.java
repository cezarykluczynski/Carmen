package com.cezarykluczynski.carmen.cron.management.iterator;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import com.cezarykluczynski.carmen.util.iterator.AnnotatedClassesIterator;

class DatabaseSwitchableJobsIterator extends AnnotatedClassesIterator {

    DatabaseSwitchableJobsIterator() {
        super(DatabaseSwitchableJob.class);
    }

}
