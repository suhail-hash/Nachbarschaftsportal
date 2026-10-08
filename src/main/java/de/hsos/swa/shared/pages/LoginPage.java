package de.hsos.swa.shared.pages;
import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.net.URI;
//Gast wird hier weitergeleitet wenn we auf Anmelden druckt.
//Wenn man auf Logout druckt wird man hier auch weitergeleitet
@Path("/login")
public class LoginPage {

    @GET
    @Authenticated
    public Response login() {
        return Response.seeOther(URI.create("/home")).build();
    }
}
