package com.cezarykluczynski.carmen.cron.languages.iterator

import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityTwo
import spock.lang.Specification

class RefreshableTableIteratorTest extends Specification {

    private RefreshableTableIterator tableIterator

    def setup() {
        tableIterator = new RefreshableTableIterator(LanguagesStatistics.class, new LanguagesIteratorsFactory())
    }

    def "iterator is not empty"() {
        expect:
        tableIterator.hasNext()
    }

    def "test entities are picked up"() {
        given:
        int pickedUpTestEntityCount = 0

        when:
        while(tableIterator.hasNext()) {
            RefreshableTable refreshableTable = tableIterator.next()
            if (refreshableTable.getBaseClass().equals(EntityOne) || refreshableTable.getBaseClass().equals(EntityTwo)) {
                pickedUpTestEntityCount++
            }
        }

        then:
        pickedUpTestEntityCount == 2
    }

}
