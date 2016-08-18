package com.cezarykluczynski.carmen.cron.languages.filter

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.iterator.EntityFieldsIterator
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory
import spock.lang.Specification

class AllFieldsFilterTest extends Specification {

    private EntityFieldsIterator entityFieldsIterator

    def setup() {
        entityFieldsIterator = new LanguagesIteratorsFactory().createEntityFieldsIterator(
                EntityOne.class, FieldsFilter.ALL)
    }

    def "entities fields iterator has correct size"() {
        expect:
        entityFieldsIterator.size() == 13
    }

}
