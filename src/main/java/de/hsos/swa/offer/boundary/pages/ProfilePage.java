package de.hsos.swa.offer.boundary.pages;

import de.hsos.swa.offer.control.OfferService;
import de.hsos.swa.offer.entity.Offer;
import de.hsos.swa.offer.gateway.acl.UserLookup;
import io.quarkus.qute.Template;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.net.URI;
import java.util.List;

import static java.util.Objects.requireNonNull;


@Path("/profile")
public class ProfilePage {
    @Inject
    JsonWebToken jwt;

    @Inject
    OfferService offerService;

    private final Template page;

    public ProfilePage(Template profile) {
        this.page = requireNonNull(profile);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Authenticated
    public Response getMyProfile() {

        UserLookup.UserRecord user = offerService.getUserByEmail(jwt.getClaim("preferred_username"));

        List<Offer> offers = offerService.getAllOffersByUserId(user.userId());

        if (user.userId() == null) {
            return Response.seeOther(
                    URI.create("/profile-setup")
            ).build();
        }

        return Response.ok(
                page.data("userId", user.userId())
                        .data("profilUserId", user.userId())
                        .data("isOwnProfile", true)
                        .data("offers", offers)
        ).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    @Authenticated
    public Response getProfile(@PathParam("id") Long id) {

        UserLookup.UserRecord user = offerService.getUserByEmail(jwt.getClaim("preferred_username"));

        List<Offer> offers = offerService.getAllOffersByUserId(id);
        return Response.ok(
                page.data("userId", user.userId())
                        .data("profilUserId", id)
                        .data("isOwnProfile", false)
                        .data("offers", offers)
        ).build();
    }
}
