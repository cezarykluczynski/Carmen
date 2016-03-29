package com.cezarykluczynski.carmen.admin.web.endpoint.api.github;

import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/admin/github/cron/users_import")
@Produces(MediaType.APPLICATION_JSON)
public interface UsersImportCron {

    @GET
    Response get();

    @PUT
    Response update(@FormParam("enabled") boolean status);

}
