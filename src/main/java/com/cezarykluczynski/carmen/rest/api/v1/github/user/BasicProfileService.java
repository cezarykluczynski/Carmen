package com.cezarykluczynski.carmen.rest.api.v1.github.user;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

@Path("/v1/github/user")
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public interface BasicProfileService {

    @GET
    @Path("/{login}/basicProfile")
    Response get(@PathParam("login") String login);

}
