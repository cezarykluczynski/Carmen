package com.cezarykluczynski.carmen.cron.languages.filter

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter
import com.cezarykluczynski.carmen.cron.languages.iterator.EntityFieldsIterator
import com.cezarykluczynski.carmen.cron.languages.iterator.fixture.Entity1
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class LanguageDiffFieldsFilterTest {

    EntityFieldsIterator entityFieldsIterator

    @BeforeMethod
    void setUp() {
        entityFieldsIterator = new EntityFieldsIterator(Entity1.class, FieldsFilter.LANGUAGE_DIFF_CURRENT)
    }

    @Test
    void filterFields() {
        Assert.assertEquals entityFieldsIterator.size(), 6
    }

}
