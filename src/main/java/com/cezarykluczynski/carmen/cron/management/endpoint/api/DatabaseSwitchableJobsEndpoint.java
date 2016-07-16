package com.cezarykluczynski.carmen.cron.management.endpoint.api;

import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/admin/cron/database_switchable_job")
@Produces(MediaType.APPLICATION_JSON)
public interface DatabaseSwitchableJobsEndpoint {

    @GET
    Response getAll();

}
