package com.cezarykluczynski.carmen.cron.management.service

import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO
import com.cezarykluczynski.carmen.dao.pub.CronsDAO
import com.cezarykluczynski.carmen.model.pub.Cron
import com.google.common.collect.Lists
import spock.lang.Specification

class DatabaseSwitchableJobsServiceTest extends Specification {

    private static final String NAME_1 = "DatabaseSwitchableJobAnnotatedMock"
    private static final String NAME_2 = "DatabaseSwitchableJobAnnotatedMock2"
    private static final String NAME_3 = "DatabaseSwitchableJobAnnotatedMock3"

    private CronsDAO cronsDAOMock

    private DatabaseSwitchableJobListProvider jobListProviderMock

    private DatabaseSwitchableJobsService service

    private List<Cron> cronList

    private Cron cronMock

    private Cron cronMock3

    def setup() {
        cronsDAOMock = Mock CronsDAO
        jobListProviderMock = Mock DatabaseSwitchableJobListProvider
        cronMock = Mock Cron
        cronMock.getName() >> NAME_1
        cronMock3 = Mock Cron
        cronMock3.getName() >> NAME_3
        cronList = Lists.newArrayList cronMock
        service = new DatabaseSwitchableJobsService(cronsDAOMock, jobListProviderMock)
    }

    def "gets all cron jobs"() {
        given:
        cronsDAOMock.findAll() >> cronList
        jobListProviderMock.getDatabaseSwitchableJobsClasses() >> Lists
                .newArrayList(DatabaseSwitchableJobAnnotatedMock.class)

        when:
        List<DatabaseSwitchableJobDTO> list = service.getAll()

        then:
        list.size() == 1
        list.get(0).getName() == NAME_1
    }

    def "job list is updated"() {
        given:
        cronsDAOMock.findAll() >> cronList
        jobListProviderMock.getDatabaseSwitchableJobsClasses() >> Lists
                .newArrayList(DatabaseSwitchableJobAnnotatedMock.class, DatabaseSwitchableJobAnnotatedMock2.class)
        cronList = Lists.newArrayList(cronMock, cronMock3)
        cronsDAOMock.findByName(NAME_3) >> Lists.newArrayList(cronMock3)

        when:
        service.updateList()

        then:
        1 * cronsDAOMock.findAll() >> cronList
        1 * cronsDAOMock.delete(_) >> { args ->
            Cron cron = (Cron) args[0]
            assert cron.name == NAME_3
        }
        1 * cronsDAOMock.create(_) >> { args ->
            Cron cron = (Cron) args[0]
            assert cron.name == NAME_2
        }
    }

    def "job is enabled"() {
        given:
        cronsDAOMock.findAll() >> cronList
        jobListProviderMock.getDatabaseSwitchableJobsClasses() >> Lists
                .newArrayList(DatabaseSwitchableJobAnnotatedMock.class)
        DatabaseSwitchableJobDTO dto = createDatabaseSwitchableJobDTO()

        when:
        service.enable dto

        then:
        1 * cronMock.setEnabled(true)
        0 * cronMock.setEnabled(false)
        1 * cronsDAOMock.update(cronMock)
    }

    def "does not enable when entity is missing"() {
        given:
        jobListProviderMock.getDatabaseSwitchableJobsClasses() >> Lists
                .newArrayList(DatabaseSwitchableJobAnnotatedMock.class)
        cronsDAOMock.findAll() >> Lists.newArrayList()
        DatabaseSwitchableJobDTO dto = createDatabaseSwitchableJobDTO()

        when:
        service.enable dto

        then:
        0 * cronMock.setEnabled(_)
        0 * cronsDAOMock.update(cronMock)
    }

    def "cron is being disabled"() {
        given:
        jobListProviderMock.getDatabaseSwitchableJobsClasses() >> Lists
                .newArrayList(DatabaseSwitchableJobAnnotatedMock.class)
        cronsDAOMock.findAll() >> cronList
        DatabaseSwitchableJobDTO dto = createDatabaseSwitchableJobDTO()

        when:
        service.disable dto

        then:
        1 * cronMock.setEnabled(false)
        0 * cronMock.setEnabled(true)
        1 * cronsDAOMock.update(cronMock)
    }

    def "cron should be enabled when it is not found"() {
        given:
        jobListProviderMock.getDatabaseSwitchableJobsClasses() >> Lists
                .newArrayList(DatabaseSwitchableJobAnnotatedMock.class)
        cronsDAOMock.findAll() >> Lists.newArrayList()

        when:
        boolean enabled = service.isEnabledOrNotDatabaseSwitchable NAME_1

        then:
        enabled
    }

    def "cron should be enabled when it is enabled"() {
        given:
        jobListProviderMock.getDatabaseSwitchableJobsClasses() >> Lists
                .newArrayList(DatabaseSwitchableJobAnnotatedMock.class)
        cronsDAOMock.findAll() >> Lists.newArrayList(
                new Cron(name: NAME_1, enabled: true)
        )

        when:
        boolean enabled = service.isEnabledOrNotDatabaseSwitchable NAME_1

        then:
        enabled
    }

    def "cron should be disabled when it is not enabled"() {
        given:
        jobListProviderMock.getDatabaseSwitchableJobsClasses() >> Lists
                .newArrayList(DatabaseSwitchableJobAnnotatedMock.class)
        cronsDAOMock.findAll() >> Lists.newArrayList(
                new Cron(name: NAME_1, enabled: false)
        )

        when:
        boolean enabled = service.isEnabledOrNotDatabaseSwitchable NAME_1

        then:
        !enabled
    }

    private static DatabaseSwitchableJobDTO createDatabaseSwitchableJobDTO() {
        return DatabaseSwitchableJobDTO.builder().name(NAME_1).build()
    }

}
