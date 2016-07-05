package com.cezarykluczynski.carmen.util.iterator

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace
import com.cezarykluczynski.carmen.model.cassandra.carmen.Commit
import org.testng.Assert
import org.testng.annotations.Test

public class AnnotatedClassesIteratorTest {


    @Test
    void test() {
        AnnotatedClassesIterator iterator = new AnnotatedClassesIterator(Keyspace.class)
        boolean hasCommitClass = false

        while(iterator.hasNext()) {
            if (iterator.next().equals(Commit.class)) {
                hasCommitClass = true
            }
        }

        Assert.assertTrue hasCommitClass

    }
}
