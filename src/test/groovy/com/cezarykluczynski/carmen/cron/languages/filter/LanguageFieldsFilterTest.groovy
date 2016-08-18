package com.cezarykluczynski.carmen.cron.languages.filter

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.iterator.EntityFieldsIterator
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory
import org.testng.Assert
import spock.lang.Specification

class LanguageFieldsFilterTest extends Specification {

    private EntityFieldsIterator entityFieldsIterator

    def setup() {
        entityFieldsIterator = new LanguagesIteratorsFactory().createEntityFieldsIterator(
                EntityOne.class, FieldsFilter.LANGUAGE_CURRENT)
    }

    def "entity fields are found"() {
        expect:
        Assert.assertEquals entityFieldsIterator.size(), 4
    }

}
