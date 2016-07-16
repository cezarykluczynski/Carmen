package com.cezarykluczynski.carmen.cron.management.endpoint.impl

import com.beust.jcommander.internal.Lists
import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO
import com.cezarykluczynski.carmen.cron.management.service.DatabaseSwitchableJobsService
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import javax.ws.rs.core.Response

import static org.mockito.Mockito.*

class DatabaseSwitchableJobsEndpointImplTest {

    private DatabaseSwitchableJobsEndpointImpl endpoint

    private DatabaseSwitchableJobsService jobsService

    @BeforeMethod
    void setup() {
        jobsService = mock DatabaseSwitchableJobsService
        when jobsService.getAll() thenReturn Lists.newArrayList(mock(DatabaseSwitchableJobDTO))
        endpoint = new DatabaseSwitchableJobsEndpointImpl(jobsService)
    }

    @Test
    void getAll() {
        Response response = endpoint.getAll()

        List<DatabaseSwitchableJobDTO> entity = (List<DatabaseSwitchableJobDTO>) response.getEntity()

        Assert.assertEquals response.getStatus(), 200
        Assert.assertEquals entity.size(), 1
        verify(jobsService).getAll()
    }


}
