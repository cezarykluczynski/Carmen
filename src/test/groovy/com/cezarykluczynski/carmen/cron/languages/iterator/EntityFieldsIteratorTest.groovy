package com.cezarykluczynski.carmen.cron.languages.iterator

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.model.EntityField
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class EntityFieldsIteratorTest {

    EntityFieldsIterator entityFieldsIterator

    @BeforeMethod
    void setUp() {
        entityFieldsIterator = new LanguagesIteratorsFactory().createEntityFieldsIterator(
                EntityOne.class, FieldsFilter.ALL)
    }

    @Test
    void fieldsAreRead() {
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("id", UUID.class)
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_1")
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_1_added")
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_1_removed")
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_2")
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_2_added")
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_2_removed")
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_3")
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_3_added")
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_3_removed")
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_4")
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_4_added")
        Assert.assertEquals entityFieldsIterator.next(), new EntityField("language_4_removed")
        Assert.assertFalse entityFieldsIterator.hasNext()
    }

}
