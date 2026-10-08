package de.hsos.swa.offer.boundary.pages;

import de.hsos.swa.offer.control.OfferService;
import de.hsos.swa.offer.entity.Offer;
import de.hsos.swa.offer.gateway.acl.UserLookup;
import de.hsos.swa.user.control.UserService;
import io.quarkus.qute.Template;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;


@Path("/home")
public class HomePage {

    @Inject
    JsonWebToken jwt;
    @Inject
    SecurityIdentity identity;
    @Inject
    OfferService offerService;

    private final Template home;

    public HomePage(Template home) {
        this.home = requireNonNull(home, "page is required");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response get() {
        // Gast: Seite ohne Nutzerdaten rendern
        if (identity.isAnonymous()) {
            return Response.ok(home.data("userId", null).data("user", null)).build();
        }
        UserLookup.UserRecord user = offerService.getUserByEmail(jwt.getClaim("email"));
        if (user == null) {
            // eingeloggt, aber noch kein Profil -> zur Einrichtung
            return Response.seeOther(URI.create("/profile-setup")).build();
        }
        return Response.ok(home
                .data("userId", user.userId())
                .data("email", user.email())
                .data("user", user)
        ).build();
    }

}
