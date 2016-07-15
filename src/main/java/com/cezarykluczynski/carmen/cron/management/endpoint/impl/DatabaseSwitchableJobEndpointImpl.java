package com.cezarykluczynski.carmen.cron.management.endpoint.impl;

import com.cezarykluczynski.carmen.cron.management.endpoint.api.DatabaseSwitchableJobEndpoint;
import com.cezarykluczynski.carmen.cron.management.service.DatabaseSwitchableJobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
@Profile("admin-panel")
public class DatabaseSwitchableJobEndpointImpl implements DatabaseSwitchableJobEndpoint {

    private DatabaseSwitchableJobsService databaseSwitchableJobsService;

    @Autowired
    public DatabaseSwitchableJobEndpointImpl(DatabaseSwitchableJobsService databaseSwitchableJobsService) {
        this.databaseSwitchableJobsService = databaseSwitchableJobsService;
    }

    @Override
    public Response getAll() {
        return Response.ok().entity(databaseSwitchableJobsService.getAll()).build();
    }

}
