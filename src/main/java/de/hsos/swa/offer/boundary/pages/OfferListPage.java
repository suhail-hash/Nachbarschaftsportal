package de.hsos.swa.offer.boundary.pages;

import de.hsos.swa.offer.control.OfferService;
import de.hsos.swa.offer.entity.Offer;
import de.hsos.swa.offer.entity.OfferCategory;
import de.hsos.swa.offer.entity.OfferCategorySubject;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;
@Path("/listOffer")
@Authenticated
public class OfferListPage {

    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    @Inject
    OfferService offerService;

    private final Template offerListPage;

    public OfferListPage(Template listOffer) {
        this.offerListPage = requireNonNull(listOffer);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response get(@QueryParam("cat") String category, @QueryParam("catsub") String categorySubject, @QueryParam("lat") double latitude, @QueryParam("lon") double longitude, @QueryParam("rad") double radius) {

        UserLookup.UserRecord user = offerService.getUserByEmail(jwt.getClaim("preferred_username"));

        logger.info("Params: cat->" + category + ", catsub->" + categorySubject +", lat->" + latitude + ", lon->" + longitude + ", rad->" + radius);

        String nope = "nein";

        try {

           List<Offer> offers = offerService.getAllOffersByParams(user.userId(), longitude, latitude, OfferCategory.valueOf(category), OfferCategorySubject.valueOf(categorySubject), radius);

           Set<Long> unknownUserIds = new HashSet<>();

           for (Offer offer : offers) {
                unknownUserIds.add(offer.getOwnerId());
            }

           Map<Long, String>  userLabels = offerService.getUserLabes(user.userId(), unknownUserIds);

           logger.info("OfferListPage: offers size: " + offers.size());
           logger.info("OfferListPage: userLabels: " + userLabels);

           TemplateInstance offerTemplate = offerListPage
                   .data("userId", user.userId())
                   .data("offers", offers)
                   .data("labels", userLabels);

           return  Response.ok(offerTemplate).build();





        } catch (NotFoundException e) {
            logger.info("Offer nicht gefunden");
            return Response.status(404).build();
        }
    }


}
