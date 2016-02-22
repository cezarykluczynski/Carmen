package com.cezarykluczynski.carmen.cron.languages.filter

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter
import com.cezarykluczynski.carmen.cron.languages.iterator.EntityFieldsIterator
import com.cezarykluczynski.carmen.cron.languages.fixture.EntityOne
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class LanguageFieldsFilterTest {

    EntityFieldsIterator entityFieldsIterator

    @BeforeMethod
    void setUp() {
        entityFieldsIterator = new EntityFieldsIterator(EntityOne.class, FieldsFilter.LANGUAGE_CURRENT)
    }

    @Test
    void filterFields() {
        Assert.assertEquals entityFieldsIterator.size(), 3
    }

}
