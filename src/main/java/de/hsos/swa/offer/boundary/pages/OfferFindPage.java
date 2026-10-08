package de.hsos.swa.offer.boundary.pages;
import de.hsos.swa.offer.control.OfferService;
import de.hsos.swa.offer.gateway.acl.UserLookup;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import static java.util.Objects.requireNonNull;

@Path("/findOffer")
@Authenticated
public class OfferFindPage {

    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    @Inject
    OfferService offerService;

    private final Template offerPage;

    public OfferFindPage(Template findOffer) {
        this.offerPage = requireNonNull(findOffer);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response get() {

        UserLookup.UserRecord user = offerService.getUserByEmail(jwt.getClaim("preferred_username"));

        String nope = "nein";

        try {
            TemplateInstance offerTemplate = offerPage
                    .data("userId", user.userId())
                    .data("radiusStart", 2)
                    .data("userId", user.userId())
                    .data("city", user.address().city())
                    .data("plz", user.address().plz())
                    .data("street", user.address().street());

            return  Response.ok(offerTemplate).build();

        } catch (NotFoundException e) {
            return Response.status(404).build();
        }
    }


}
