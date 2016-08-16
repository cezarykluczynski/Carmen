package com.cezarykluczynski.carmen.cron.management.service

import spock.lang.Specification

class DatabaseSwitchableJobListProviderTest extends Specification {

    private DatabaseSwitchableJobListProvider provider

    def setup() {
        provider = new DatabaseSwitchableJobListProvider()
    }

    def "database switchable jobs classes list is not empty"() {
        expect:
        !provider.getDatabaseSwitchableJobsClasses().isEmpty()
    }

}
