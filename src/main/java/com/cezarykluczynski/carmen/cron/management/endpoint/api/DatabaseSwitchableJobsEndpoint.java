package com.cezarykluczynski.carmen.cron.management.endpoint.api;

import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/admin/cron/database_switchable_job")
@Produces(MediaType.APPLICATION_JSON)
public interface DatabaseSwitchableJobsEndpoint {

    @GET
    Response getAll();

    @POST
    @Path("/update_list")
    Response updateList();

    @POST
    @Path("/enable")
    Response enable(@FormParam("name") String name);

    @POST
    @Path("/disable")
    Response disable(@FormParam("name") String name);

}
