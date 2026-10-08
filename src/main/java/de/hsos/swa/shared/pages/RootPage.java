package de.hsos.swa.shared.pages;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.net.URI;
@Path("/")
public class RootPage {

    //nicht Authenticated fuer Gast
    @GET
    public Response root() {
        return Response.seeOther(URI.create("/home")).build();
    }
}