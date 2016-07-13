package com.cezarykluczynski.carmen.cron.management.service

import com.beust.jcommander.internal.Lists
import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO
import com.cezarykluczynski.carmen.dao.pub.CronsDAO
import com.cezarykluczynski.carmen.model.pub.Cron
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.doNothing
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.when
import static org.mockito.Mockito.verify

class DatabaseSwitchableJobsServiceTest {

    private static final String NAME = "DatabaseSwitchableJobAnnotatedMock"

    private CronsDAO cronsDAOMock

    private DatabaseSwitchableJobListProvider jobListProviderMock

    private DatabaseSwitchableJobsService service

    private List<Cron> cronList

    private Cron cronMock

    @BeforeMethod
    void setup() {
        cronsDAOMock = mock CronsDAO
        jobListProviderMock = mock DatabaseSwitchableJobListProvider
        when jobListProviderMock.getDatabaseSwitchableJobsClasses() thenReturn Lists
                .newArrayList(DatabaseSwitchableJobAnnotatedMock.class)
        cronMock = mock Cron
        when cronMock.getName() thenReturn NAME
        doNothing().when(cronMock).setEnabled true
        doNothing().when(cronMock).setEnabled false
        cronList = Lists.newArrayList(cronMock)
        when cronsDAOMock.findAll() thenReturn cronList
        doNothing().when(cronsDAOMock).update cronMock
        service = new DatabaseSwitchableJobsService(cronsDAOMock, jobListProviderMock)
    }

    @Test
    void getAll() {
        // exercise
        List<DatabaseSwitchableJobDTO> list = service.getAll()

        // assertion
        Assert.assertEquals list.size(), 1
        Assert.assertEquals list.get(0).getName(), NAME
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
        cronList.remove(0)
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

    private static DatabaseSwitchableJobDTO createDatabaseSwitchableJobDTO() {
        return DatabaseSwitchableJobDTO.builder().name(NAME).build()
    }

}
