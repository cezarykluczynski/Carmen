package com.cezarykluczynski.carmen.cron.languages.iterator

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.iterator.fixture.Entity1
import com.cezarykluczynski.carmen.cron.languages.iterator.fixture.Entity2
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class TableIteratorTest {

    TableIterator tableIterator

    @BeforeMethod
    void setUp() {
        tableIterator = new TableIterator()
    }

    @Test
    void iteratorIsNotEmpty() {
        Assert.assertTrue tableIterator.hasNext()
    }

    @Test
    void testEntitiesArePickedUp() {
        int pickedUpTestEntityCount = 0

        while(tableIterator.hasNext()) {
            RefreshableTable refreshableTable = tableIterator.next()
            if (refreshableTable instanceof Entity1) {
                pickedUpTestEntityCount++
            }
            if (refreshableTable instanceof Entity2) {
                pickedUpTestEntityCount++
            }
        }

        Assert.assertEquals pickedUpTestEntityCount, 2
    }

}
