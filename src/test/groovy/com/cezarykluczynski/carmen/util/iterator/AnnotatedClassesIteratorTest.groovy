package com.cezarykluczynski.carmen.util.iterator

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace
import com.cezarykluczynski.carmen.model.cassandra.carmen.Commit
import spock.lang.Specification

class AnnotatedClassesIteratorTest extends Specification {

    void "detects that Commit class has keyspace annotation"() {
        given:
        AnnotatedClassesIterator iterator = new AnnotatedClassesIterator(Keyspace.class)
        boolean hasCommitClass = false

        when:
        while(iterator.hasNext()) {
            if (iterator.next().equals(Commit.class)) {
                hasCommitClass = true
            }
        }

        then:
        hasCommitClass
    }

}
