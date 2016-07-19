package com.cezarykluczynski.carmen.cron.management.service

import com.beust.jcommander.internal.Lists
import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO
import com.cezarykluczynski.carmen.dao.pub.CronsDAO
import com.cezarykluczynski.carmen.model.pub.Cron
import org.mockito.ArgumentCaptor
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Matchers.any
import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.when
import static org.mockito.Mockito.verify

class DatabaseSwitchableJobsServiceTest {

    private static final String NAME_1 = "DatabaseSwitchableJobAnnotatedMock"
    private static final String NAME_2 = "DatabaseSwitchableJobAnnotatedMock2"
    private static final String NAME_3 = "DatabaseSwitchableJobAnnotatedMock3"

    private CronsDAO cronsDAOMock

    private DatabaseSwitchableJobListProvider jobListProviderMock

    private DatabaseSwitchableJobsService service

    private List<Cron> cronList

    private Cron cronMock

    private Cron cronMock3

    @BeforeMethod
    void setup() {
        cronsDAOMock = mock CronsDAO
        jobListProviderMock = mock DatabaseSwitchableJobListProvider
        cronMock = mock Cron
        when cronMock.getName() thenReturn NAME_1
        cronMock3 = mock Cron
        when cronMock3.getName() thenReturn NAME_3
        when jobListProviderMock.getDatabaseSwitchableJobsClasses() thenReturn Lists
                .newArrayList(DatabaseSwitchableJobAnnotatedMock.class)
        doNothing().when(cronMock).setEnabled true
        doNothing().when(cronMock).setEnabled false
        doNothing().when(cronsDAOMock).update cronMock
        cronList = Lists.newArrayList cronMock
        when cronsDAOMock.findAll() thenReturn cronList
        when(cronsDAOMock.create(any(Cron.class))).thenReturn null
        doNothing().when(cronsDAOMock).delete(any(Cron.class))
        service = new DatabaseSwitchableJobsService(cronsDAOMock, jobListProviderMock)
    }

    @Test
    void getAll() {
        // exercise
        List<DatabaseSwitchableJobDTO> list = service.getAll()

        // assertion
        Assert.assertEquals list.size(), 1
        Assert.assertEquals list.get(0).getName(), NAME_1
    }

    @Test
    void updateList() {
        // setup
        when jobListProviderMock.getDatabaseSwitchableJobsClasses() thenReturn Lists
                .newArrayList(DatabaseSwitchableJobAnnotatedMock.class, DatabaseSwitchableJobAnnotatedMock2.class)
        cronList = Lists.newArrayList(cronMock, cronMock3)
        when cronsDAOMock.findAll() thenReturn cronList
        when cronsDAOMock.findByName(NAME_3) thenReturn Lists.newArrayList(cronMock3)

        // exercise
        service.updateList()

        // assertion
        verify(cronsDAOMock).findAll()
        ArgumentCaptor<Cron> cronArgumentCaptor = ArgumentCaptor.forClass Cron.class
        verify(cronsDAOMock).create(cronArgumentCaptor.capture())
        cronArgumentCaptor.allValues.get(0).name == NAME_2
        verify(cronsDAOMock).delete(cronArgumentCaptor.capture())
        cronArgumentCaptor.allValues.get(0).name == NAME_3
    }


    @Test
    void enable() {
        // setup
        DatabaseSwitchableJobDTO dto = createDatabaseSwitchableJobDTO()

        // exercise
        service.enable dto

        // assertion
        verify(cronMock).setEnabled true
        verify(cronMock, never()).setEnabled false
        verify(cronsDAOMock).update cronMock
    }


    @Test
    void "does not enable when entity is missing"() {
        // setup
        when cronsDAOMock.findAll() thenReturn Lists.newArrayList()
        DatabaseSwitchableJobDTO dto = createDatabaseSwitchableJobDTO()

        // exercise
        service.enable dto

        // assertion
        verify(cronMock, never()).setEnabled true
        verify(cronMock, never()).setEnabled false
        verify(cronsDAOMock, never()).update cronMock
    }

    @Test
    void disable() {
        // setup
        DatabaseSwitchableJobDTO dto = createDatabaseSwitchableJobDTO()

        // exercise
        service.disable dto

        // assertion
        verify(cronMock).setEnabled false
        verify(cronMock, never()).setEnabled true
        verify(cronsDAOMock).update cronMock
    }

    @Test
    void "cron should be enabled when it is not found"() {
        // setup
        when cronsDAOMock.findAll() thenReturn Lists.newArrayList()

        // exercise
        boolean enabled = service.isEnabledOrNotDatabaseSwitchable NAME_1

        // assertion
        Assert.assertTrue enabled
    }

    @Test
    void "cron should be enabled when it is enabled"() {
        // setup
        when cronsDAOMock.findAll() thenReturn Lists.newArrayList(
                new Cron(name: NAME_1, enabled: true)
        )

        // exercise
        boolean enabled = service.isEnabledOrNotDatabaseSwitchable NAME_1

        // assertion
        Assert.assertTrue enabled
    }


    @Test
    void "cron should be disabled when it is not enabled"() {
        // setup
        when cronsDAOMock.findAll() thenReturn Lists.newArrayList(
                new Cron(name: NAME_1, enabled: false)
        )

        // exercise
        boolean enabled = service.isEnabledOrNotDatabaseSwitchable NAME_1

        // assertion
        Assert.assertFalse enabled
    }

    private static DatabaseSwitchableJobDTO createDatabaseSwitchableJobDTO() {
        return DatabaseSwitchableJobDTO.builder().name(NAME_1).build()
    }

}
