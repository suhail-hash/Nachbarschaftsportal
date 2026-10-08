package de.hsos.swa.offer.boundary.pages;
import de.hsos.swa.offer.control.OfferService;
import de.hsos.swa.offer.entity.Offer;
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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@Path("/showOffer")
@Authenticated
public class OfferShowPage {

    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    @Inject
    OfferService offerService;

    private final Template offerShow;

    public OfferShowPage(Template showOffer) {
        this.offerShow = requireNonNull(showOffer);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response get(@PathParam("id") Long offerId) {
        UserLookup.UserRecord user = offerService.getUserByEmail(jwt.getClaim("preferred_username"));

        try {
            Offer offer = offerService.getOfferById(offerId)
                    .orElseThrow(NotFoundException::new);

            Set<Long> unknownUserIds = new HashSet<>();
            unknownUserIds.add(offer.getOwnerId());

            Map<Long, String> userLabels = offerService.getUserLabes(user.userId(), unknownUserIds);

            TemplateInstance offerTemplate = offerShow
                    .data("userId", user.userId())
                    .data("id", offer.getId())
                    .data("title", offer.getTitle())
                    .data("description", offer.getDescription())
                    .data("status", offer.getStatus())
                    .data("category", offer.getCategory())
                    .data("categorySubject", offer.getCategorySubject())
                    .data("privacy", offer.getPrivacy())
                    .data("ownerId", offer.getOwnerId())
                    .data("longitudeCity", offer.getLongitudeCity())
                    .data("latitudeCity", offer.getLatitudeCity())
                    .data("longitudeAddress", offer.getLongitudeAddress())
                    .data("latitudeAddress", offer.getLatitudeAddress())
                    .data("price", offer.getPrice())
                    .data("imageName", offer.getImageName())
                    .data("addition", offer.getAddition())
                    .data("label", userLabels);


            return  Response.ok(offerTemplate).build();

        } catch (NotFoundException e) {
            logger.info("Offer " + offerId + " nicht gefunden");
            return Response.status(404).build();
        }
    }


}
