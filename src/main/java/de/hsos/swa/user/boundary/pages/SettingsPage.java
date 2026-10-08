package de.hsos.swa.user.boundary.pages;

import de.hsos.swa.user.control.UserService;
import io.quarkus.qute.Template;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.net.URI;

import static java.util.Objects.requireNonNull;

@Path("/settings")
public class SettingsPage {

    @Inject
    JsonWebToken jwt;
    @Inject
    UserService userService;
    private final Template page;

    public SettingsPage(Template settings) {
        this.page = requireNonNull(settings);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Authenticated
    public Response get() {
        String email = jwt.getClaim("email");
        Long id = userService.idByEmail(email);
        if (id == null) {
            return Response.seeOther(URI.create("/home")).build();
        }
        return Response.ok(
                page.data("userId", id)
                        .data("email", email)
        ).build();
    }
}
