package com.cezarykluczynski.carmen.cron.languages.iterator

import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.iterator.fixture.Entity1
import com.cezarykluczynski.carmen.cron.languages.iterator.fixture.Entity2
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class RefreshableTableIteratorTest {

    RefreshableTableIterator tableIterator

    @BeforeMethod
    void setUp() {
        tableIterator = new RefreshableTableIterator(LanguagesStatistics.class)
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
            if (refreshableTable.getBaseClass().equals(Entity1) || refreshableTable.getBaseClass().equals(Entity2)) {
                pickedUpTestEntityCount++
            }
        }

        Assert.assertEquals pickedUpTestEntityCount, 2
    }

}
