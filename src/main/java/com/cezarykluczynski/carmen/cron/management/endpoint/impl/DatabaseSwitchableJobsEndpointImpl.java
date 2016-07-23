package com.cezarykluczynski.carmen.cron.management.endpoint.impl;

import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO;
import com.cezarykluczynski.carmen.cron.management.endpoint.api.DatabaseSwitchableJobsEndpoint;
import com.cezarykluczynski.carmen.cron.management.service.DatabaseSwitchableJobsService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
@Profile("admin-panel")
public class DatabaseSwitchableJobsEndpointImpl implements DatabaseSwitchableJobsEndpoint {

    private DatabaseSwitchableJobsService databaseSwitchableJobsService;

    @Autowired
    public DatabaseSwitchableJobsEndpointImpl(DatabaseSwitchableJobsService databaseSwitchableJobsService) {
        this.databaseSwitchableJobsService = databaseSwitchableJobsService;
    }

    @Override
    public Response getAll() {
        return Response.ok().entity(databaseSwitchableJobsService.getAll()).build();
    }

    @Override
    public Response updateList() {
        databaseSwitchableJobsService.updateList();
        return Response.ok(Lists.newArrayList()).build();
    }

    @Override
    public Response enable(String name) {
        DatabaseSwitchableJobDTO dto = DatabaseSwitchableJobDTO.builder().name(name).build();
        databaseSwitchableJobsService.enable(dto);
        return Response.ok(dto).build();
    }

    @Override
    public Response disable(String name) {
        DatabaseSwitchableJobDTO dto = DatabaseSwitchableJobDTO.builder().name(name).build();
        databaseSwitchableJobsService.disable(dto);
        return Response.ok(dto).build();
    }
}
