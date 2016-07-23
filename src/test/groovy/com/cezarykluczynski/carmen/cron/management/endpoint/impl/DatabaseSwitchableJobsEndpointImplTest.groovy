package com.cezarykluczynski.carmen.cron.management.endpoint.impl

import com.beust.jcommander.internal.Lists
import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO
import com.cezarykluczynski.carmen.cron.management.service.DatabaseSwitchableJobsService
import org.json.JSONObject
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import javax.ws.rs.core.Response

import static org.mockito.Mockito.*

class DatabaseSwitchableJobsEndpointImplTest {

    private static final String NAME = "name"

    private DatabaseSwitchableJobsEndpointImpl endpoint

    private DatabaseSwitchableJobsService jobsService

    @BeforeMethod
    void setup() {
        jobsService = mock DatabaseSwitchableJobsService
        when jobsService.getAll() thenReturn Lists.newArrayList(mock(DatabaseSwitchableJobDTO))
        doNothing().when(jobsService).updateList()
        endpoint = new DatabaseSwitchableJobsEndpointImpl(jobsService)
    }

    @Test
    void getAll() {
        // exercise
        Response response = endpoint.getAll()

        // assertion
        List<DatabaseSwitchableJobDTO> entity = (List<DatabaseSwitchableJobDTO>) response.getEntity()

        Assert.assertEquals response.getStatus(), 200
        Assert.assertEquals entity.size(), 1
        verify(jobsService).getAll()
    }

    @Test
    void updateList() {
        // exercise
        Response response = endpoint.updateList()

        // assertion
        Assert.assertEquals response.getStatus(), 200
        verify(jobsService).updateList()
    }

    @Test
    void enable() {
        // setup
        doNothing().when(jobsService).enable(any(DatabaseSwitchableJobDTO.class))

        // exercise
        Response response = endpoint.enable NAME

        // assertion
        JSONObject responseBody = new JSONObject(response.getEntity())
        verify(jobsService).enable(any(DatabaseSwitchableJobDTO.class))
        Assert.assertEquals response.getStatus(), 200
        Assert.assertEquals responseBody.getString("name"), NAME
    }

    @Test
    void disable() {
        // setup
        doNothing().when(jobsService).disable(any(DatabaseSwitchableJobDTO.class))

        // exercise
        Response response = endpoint.disable NAME

        // assertion
        JSONObject responseBody = new JSONObject(response.getEntity())
        verify(jobsService).disable(any(DatabaseSwitchableJobDTO.class))
        Assert.assertEquals response.getStatus(), 200
        Assert.assertEquals responseBody.getString("name"), NAME
    }

}
