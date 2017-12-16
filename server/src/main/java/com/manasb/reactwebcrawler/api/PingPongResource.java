package com.manasb.reactwebcrawler.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class PingPongResource {

    @Path("/ping")
    @GET
    public Response ping() {
        return Response.ok().entity("pong").build();
    }
}
