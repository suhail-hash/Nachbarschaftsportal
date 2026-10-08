package de.hsos.swa.offer.boundary.pages;

import de.hsos.swa.offer.control.OfferService;
import de.hsos.swa.offer.entity.*;
import de.hsos.swa.offer.gateway.acl.UserLookup;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;
import static java.util.Objects.requireNonNull;
@RequestScoped
@Transactional(REQUIRES_NEW)
@Path("/newOffer")
@Authenticated
public class OfferCreatePage {


    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    @Inject
    OfferService offerService;

    private final Template offerCreate;

    public OfferCreatePage(Template newOffer) {
        this.offerCreate = requireNonNull(newOffer);
    }


    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response get() {

        UserLookup.UserRecord user = offerService.getUserByEmail(jwt.getClaim("preferred_username"));

        if (user == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {

            TemplateInstance offerTemplate = offerCreate
                    .data("userId", user.userId())
                    .data("id", 0)
                    .data("title", "")
                    .data("description", "")
                    .data("category", OfferCategory.FOR_SALE)
                    .data("categorySubject", OfferCategorySubject.ELECTRONICS)
                    .data("privacy", OfferPrivacy.PUBLIC)
                    .data("ownerId", "")
                    .data("longitudeCity", "")
                    .data("latitudeCity", "")
                    .data("longitudeAddress", "")
                    .data("latitudeAddress", "")
                    .data("price", "")
                    .data("imageName", "")
                    .data("addition", "")
                    .data("userId", user.userId())
                    .data("city", user.address().city())
                    .data("plz", user.address().plz())
                    .data("street", user.address().street())
                    .data("status", OfferStatus.AVAILABLE);

            return Response.ok(offerTemplate).build();

        }

    }


    // https://quarkus.io/guides/rest#multipart
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createOffer(
            @RestForm("title") String title,
            @RestForm("description") String description,
            @RestForm("category") String category,
            @RestForm("categorySubject") String categorySubject,
            @RestForm("ownerId") Long ownerId,
            @RestForm("offerId") Long offerId,
            @RestForm("privacy") String privacy,
            @RestForm("longitudeAddress") double longitudeAddress,
            @RestForm("latitudeAddress") double latitudeAddress,
            @RestForm("latitudeCity") double latitudeCity,
            @RestForm("longitudeCity") double longitudeCity,
            @RestForm("price") BigDecimal price,
            @RestForm("addition") String addition,
            @RestForm("image") FileUpload file,
            @RestForm("status") String status
    ) throws IOException {
        UserLookup.UserRecord user = offerService.getUserByEmail(jwt.getClaim("preferred_username"));
        logger.info("Status:" + status);
        String imageName = "38ca94a2-9015-43d7-a50d-527e53de8f52.png";

        if (file != null && file.size() > 0) {

            String imageNameOrg = file.fileName();

            String extension = "";
            int position = imageNameOrg.lastIndexOf(".");

            if (position >= 0) {
                extension = imageNameOrg.substring(position);
            }

            imageName = UUID.randomUUID() + extension;
            java.nio.file.Path path = java.nio.file.Paths.get("src/main/resources/META-INF/resources/img/offer", imageName); //Nameskonflikte wegen der @Path Annotation von Quarkus, deshalb ausgeschrieben
            Files.copy(file.filePath(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        Offer oldOffer;
        if (offerId != 0) {
            Optional<Offer> optionalOffer = offerService.getOfferById(offerId);

            if (optionalOffer.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                oldOffer = optionalOffer.get();
                if (!Objects.equals(oldOffer.getOwnerId(), user.userId())) {
                    throw new WebApplicationException(Response.Status.FORBIDDEN);
                } else {
                    offerService.updateOffer(offerId, new Offer(
                                    title,
                                    description,
                                    OfferCategory.valueOf(category),
                                    OfferCategorySubject.valueOf(categorySubject),
                                    user.userId(),
                                    OfferPrivacy.valueOf(privacy),
                                    longitudeAddress,
                                    latitudeAddress,
                                    latitudeCity,
                                    longitudeCity,
                                    price,
                                    imageName,
                                    addition,
                                    OfferStatus.valueOf(status)
                            )
                    );

                    return Response.seeOther(URI.create("/showOffer/" + offerId)).build();

                }
            }
        } else if (!Objects.equals(user.userId(), ownerId)) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        } else {

            Offer newOffer = new Offer(title, description, OfferCategory.valueOf(category), OfferCategorySubject.valueOf(categorySubject), user.userId(), OfferPrivacy.valueOf(privacy), longitudeAddress, latitudeAddress, latitudeCity, longitudeCity, price, imageName, addition, OfferStatus.valueOf(status));
            Long newOfferId = offerService.createOffer(newOffer);

            return Response.seeOther(URI.create("/showOffer/" + newOfferId)).build();
        }

}

@GET
@Path("/{id}")
@Produces(MediaType.TEXT_HTML)
public Response updateOffer(@PathParam("id") Long offerId) {

    UserLookup.UserRecord user = offerService.getUserByEmail(jwt.getClaim("preferred_username"));
    logger.info("Updating offer with id " + user.userId());
    Offer offer;

    Optional<Offer> optionalOffer = offerService.getOfferById(offerId);

    if (optionalOffer.isEmpty()) {
        return Response.status(Response.Status.NOT_FOUND).build();
    } else {
        offer = optionalOffer.get();
    }

    TemplateInstance offerTemplate = offerCreate
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
            .data("addition", offer.getAddition())
            .data("imageName", offer.getImageName())
            .data("city", "-")
            .data("plz", "00000")
            .data("street", "Bitte neu eingeben wenn du den Standort deiner ändern möchtest...")
            .data("userId", user.userId());

    return Response.ok(offerTemplate).build();


}
/*
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response testSubmit(@FormParam("title") String title){

        logger.info("Submitting offer with title: " + title);


        return Response.seeOther(URI.create("/showOffer")).build();
    }

 */
}
