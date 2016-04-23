package com.cezarykluczynski.carmen.cron.languages.iterator

import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityTwo
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class RefreshableTableIteratorTest {

    private RefreshableTableIterator tableIterator

    @BeforeMethod
    void setUp() {
        tableIterator = new RefreshableTableIterator(LanguagesStatistics.class, new LanguagesIteratorsFactory())
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
            if (refreshableTable.getBaseClass().equals(EntityOne) || refreshableTable.getBaseClass().equals(EntityTwo)) {
                pickedUpTestEntityCount++
            }
        }

        Assert.assertEquals pickedUpTestEntityCount, 2
    }

}
