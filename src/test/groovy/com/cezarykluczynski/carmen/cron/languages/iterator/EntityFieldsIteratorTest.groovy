package com.cezarykluczynski.carmen.cron.languages.iterator

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter
import com.cezarykluczynski.carmen.cron.languages.iterator.fixture.Entity1
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class EntityFieldsIteratorTest {

    EntityFieldsIterator entityFieldsIterator

    @BeforeMethod
    void setUp() {
        entityFieldsIterator = new EntityFieldsIterator(Entity1.class, FieldsFilter.ALL)
    }

    @Test
    void fieldsAreRead() {
        Assert.assertEquals entityFieldsIterator.next(), "language_1"
        Assert.assertEquals entityFieldsIterator.next(), "language_1_added"
        Assert.assertEquals entityFieldsIterator.next(), "language_1_removed"
        Assert.assertEquals entityFieldsIterator.next(), "language_2"
        Assert.assertEquals entityFieldsIterator.next(), "language_2_added"
        Assert.assertEquals entityFieldsIterator.next(), "language_2_removed"
        Assert.assertEquals entityFieldsIterator.next(), "language_3"
        Assert.assertEquals entityFieldsIterator.next(), "language_3_added"
        Assert.assertEquals entityFieldsIterator.next(), "language_3_removed"
        Assert.assertFalse entityFieldsIterator.hasNext()
    }

}
