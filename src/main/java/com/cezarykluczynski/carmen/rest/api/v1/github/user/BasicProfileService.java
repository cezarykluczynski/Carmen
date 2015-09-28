package com.cezarykluczynski.carmen.rest.api.v1.github.user;

import com.cezarykluczynski.carmen.pojo.rest.api.v1.github.user.BasicProfilePOJO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/v1/github/user")
@Produces("application/json")
public interface BasicProfileService {

    @GET
    @Path("/{login}/basicProfile")
    public BasicProfilePOJO get(@PathParam("login") String login);

}
