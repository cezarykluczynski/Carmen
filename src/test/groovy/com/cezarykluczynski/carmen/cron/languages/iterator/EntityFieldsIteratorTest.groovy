package com.cezarykluczynski.carmen.cron.languages.iterator

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.model.EntityField
import spock.lang.Specification

class EntityFieldsIteratorTest extends Specification {

    private EntityFieldsIterator entityFieldsIterator

    def setup() {
        entityFieldsIterator = new LanguagesIteratorsFactory().createEntityFieldsIterator(
                EntityOne.class, FieldsFilter.ALL)
    }

    def "fields are read"() {
        expect:
        entityFieldsIterator.next() == new EntityField("id", UUID.class)
        entityFieldsIterator.next() == new EntityField("language_1")
        entityFieldsIterator.next() == new EntityField("language_1_added")
        entityFieldsIterator.next() == new EntityField("language_1_removed")
        entityFieldsIterator.next() == new EntityField("language_2")
        entityFieldsIterator.next() == new EntityField("language_2_added")
        entityFieldsIterator.next() == new EntityField("language_2_removed")
        entityFieldsIterator.next() == new EntityField("language_3")
        entityFieldsIterator.next() == new EntityField("language_3_added")
        entityFieldsIterator.next() == new EntityField("language_3_removed")
        entityFieldsIterator.next() == new EntityField("language_4")
        entityFieldsIterator.next() == new EntityField("language_4_added")
        entityFieldsIterator.next() == new EntityField("language_4_removed")
        !entityFieldsIterator.hasNext()
    }

}
