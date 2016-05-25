package com.cezarykluczynski.carmen.admin.web.endpoint.api.github;

import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Controller
@Path("/admin/github/job/languages_list_update")
@Produces(MediaType.APPLICATION_JSON)
public interface LanguagesListUpdateJob {

    @GET
    Response getStatus();

    @POST
    Response run();

}
