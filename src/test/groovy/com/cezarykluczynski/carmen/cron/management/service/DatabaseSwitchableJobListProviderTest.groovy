package com.cezarykluczynski.carmen.cron.management.service

import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class DatabaseSwitchableJobListProviderTest {

    private DatabaseSwitchableJobListProvider provider

    @BeforeMethod
    void setup() {
        provider = new DatabaseSwitchableJobListProvider()
    }

    @Test
    void getDatabaseSwitchableJobsClasses() {
         Assert.assertFalse provider.getDatabaseSwitchableJobsClasses().isEmpty()
    }

}
