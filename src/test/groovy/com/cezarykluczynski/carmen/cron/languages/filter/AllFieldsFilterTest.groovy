package com.cezarykluczynski.carmen.cron.languages.filter

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter
import com.cezarykluczynski.carmen.cron.languages.iterator.EntityFieldsIterator
import com.cezarykluczynski.carmen.cron.languages.fixture.EntityOne
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class AllFieldsFilterTest {

    EntityFieldsIterator entityFieldsIterator

    @BeforeMethod
    void setUp() {
        entityFieldsIterator = new EntityFieldsIterator(EntityOne.class, FieldsFilter.ALL)
    }

    @Test
    void filterFields() {
        Assert.assertEquals entityFieldsIterator.size(), 10
    }

}
