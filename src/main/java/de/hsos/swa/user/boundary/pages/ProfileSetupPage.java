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

/**
 * Die Seite muss aufgerufen werden nur zum ersten Mal, wenn einen neuen User angelegt wird.
 */

@Path("/profile-setup")
@Authenticated
public class ProfileSetupPage {
    @Inject
    JsonWebToken jwt;

    @Inject
    UserService userService;

    private final Template profileSetup;

    public ProfileSetupPage(Template profileSetup) {
        this.profileSetup = requireNonNull(profileSetup);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response get() {
        Long id = userService.idByEmail(jwt.getClaim("email"));
        if (id != null) {
            return Response.seeOther(URI.create("/home")).build();  // schon registriert
        }
        return Response.ok(profileSetup.instance()).build();
    }
}
