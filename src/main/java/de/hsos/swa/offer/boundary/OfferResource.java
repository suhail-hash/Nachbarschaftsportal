package de.hsos.swa.offer.boundary;

import de.hsos.swa.offer.boundary.dto.OfferRequestDTO;
import de.hsos.swa.offer.boundary.dto.OfferDTO;

import de.hsos.swa.offer.control.OfferService;
import de.hsos.swa.offer.entity.Offer;
import de.hsos.swa.offer.gateway.acl.UserLookup;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;
@RequestScoped
@Transactional(REQUIRES_NEW)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/offers")
public class OfferResource {

    @Inject
    OfferService offerService;

    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/{id}")
    @Retry(maxRetries = 2)
    @Fallback(fallbackMethod = "getOfferByIdFallback")
    @Authenticated
    public Response getOfferById(@PathParam("id") Long id) {
        Optional<Offer> offer = offerService.getOfferById(id);
        if (offer.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        else {
            return Response.ok(OfferDTO.fromEntity(offer.get())).build();
        }
    }
    private Response getOfferByIdFallback(Long id) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Das Angebot ist momentan nicht verfügbar")
                .build();
    }

    @GET
    @Path("/user/{id}")
    @Authenticated
    public Response getOfferByUserId(@PathParam("id") Long id) {
        List<OfferDTO> dtos = offerService.getAllOffersByUserId(id).stream()
                .map(OfferDTO::fromEntity)
                .toList();
        return Response.ok(dtos).build();
    }

    @POST
    @Path("/search")
    public Response getOfferByParams(OfferRequestDTO dto) {

        Long userId = null;

        UserLookup.UserRecord user = offerService.getUserByEmail(jwt.getClaim("preferred_username"));
        if(user != null) {
            userId = user.userId();
        }

        logger.info("getOfferByParams: " + dto.category());

            List<OfferDTO> dtos =  offerService.getAllOffersByParams(userId, dto.longitude(), dto.latitude(), dto.category(), dto.categorySubject(), dto.radius()).stream()
                    .map(OfferDTO::fromEntity)
                    .toList();
            return Response.ok(dtos).build();
    }

    @POST
    @Authenticated
    public Response createOffer(OfferDTO dto) {

        logger.info("createOffer: " + dto);

        Long id = offerService.createOffer(dto.toEntity());
        return Response.ok(id).build();

    }

    @GET
    @RolesAllowed("Admin")
    public Response getAllOffers() {
        List<OfferDTO> dtos = offerService.getAllOffers().stream()
                .map(OfferDTO::fromEntity)
                .toList();
        return Response.ok(dtos).build();
    }

    @PATCH
    @Path("/{id}")
    @Authenticated
    public Response updateOffer(@PathParam("id") Long id, OfferDTO dto) {
        try {
            Offer updated = offerService.updateOffer(id, dto.toEntity());
            return Response.ok(OfferDTO.fromEntity(updated)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Authenticated
    public Response deleteOffer(@PathParam("id") Long id) {
        return offerService.deleteOffer(id)
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }



}
