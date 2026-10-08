package de.hsos.swa.friendship.boundary;


import de.hsos.swa.friendship.boundary.dto.FriendRequestDTO;
import de.hsos.swa.friendship.boundary.dto.FriendshipViewDTO;
import de.hsos.swa.friendship.control.FriendshipService;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Tag(name = "Freundschaften", description = "Verwaltung von Freundschaftsanfragen und Freundeslisten")
@Path("/friendships")
@RequestScoped
public class FriendshipResource {
    @Inject
    FriendshipService service;

    @Inject
    JsonWebToken jwt;

    @Timeout(2000)
    @Retry(maxRetries = 1)
    @GET
    @Authenticated
    @Path("/requests")
    @Operation(summary = "Eingehende Freundschaftsanfragen abrufen", description = "Liefert alle offenen Anfragen an den angemeldeten Nutzer.")
    @APIResponses({@APIResponse(responseCode = "200", description = "Anfragen wurden erfolgreich geladen"), @APIResponse(responseCode = "403", description = "Für den angemeldeten Nutzer wurde noch kein Profil angelegt")})
    public Response requests() {
        Long me = currentUserId();
        if (me == null) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Profil noch nicht angelegt").build();
        }
        List<FriendshipViewDTO> list = service.requestViewsFor(me).stream()
                .map(FriendshipViewDTO::from)
                .toList();
        return Response.ok(list).build();
    }

    @Timeout(2000)
    @Retry(maxRetries = 1)
    @GET
    @Authenticated
    @Operation(summary = "Eigene Freunde abrufen", description = "Liefert alle bestätigten Freundschaften des angemeldeten Nutzers.")
    @APIResponses({@APIResponse(responseCode = "200", description = "Freundesliste wurde erfolgreich geladen"), @APIResponse(responseCode = "403", description = "Für den angemeldeten Nutzer wurde noch kein Profil angelegt")})
    public Response myFriends() {
        Long me = currentUserId();
        if (me == null) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Profil noch nicht angelegt").build();
        }
        List<FriendshipViewDTO> list = service.friendViewsOf(me).stream()
                .map(FriendshipViewDTO::from)
                .toList();
        return Response.ok(list).build();
    }


    @POST
    @Authenticated
    @Operation(summary = "Freundschaftsanfrage senden", description = "Sendet eine neue Freundschaftsanfrage an einen anderen Nutzer.")
    @APIResponses({@APIResponse(responseCode = "201", description = "Freundschaftsanfrage wurde erfolgreich erstellt"), @APIResponse(responseCode = "400", description = "Die Anfrage ist ungültig"), @APIResponse(responseCode = "403", description = "Für den angemeldeten Nutzer wurde noch kein Profil angelegt")})
    public Response send(@Valid FriendRequestDTO dto) {
        Long me = currentUserId();
        if (me == null) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Profil noch nicht angelegt").build();
        }
        Long id = service.sendRequest(me, dto.addresseeId());
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.created(URI.create("/friendships/" + id)).build();
    }

    @PUT
    @Authenticated
    @Path("/{id}/accept")
    @Operation(summary = "Freundschaftsanfrage annehmen", description = "Nimmt eine an den angemeldeten Nutzer gerichtete Anfrage an.")
    @APIResponses({@APIResponse(responseCode = "204", description = "Anfrage wurde erfolgreich angenommen"), @APIResponse(responseCode = "403", description = "Für den angemeldeten Nutzer wurde noch kein Profil angelegt"), @APIResponse(responseCode = "404", description = "Anfrage wurde nicht gefunden")})
    public Response accept(@PathParam("id") @Positive Long id) {
        Long me = currentUserId();
        if (me == null) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Profil noch nicht angelegt").build();
        }
        boolean ok = service.accept(id, me);
        if (!ok) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @PUT
    @Authenticated
    @Path("/{id}/decline")
    @Operation(summary = "Freundschaftsanfrage ablehnen", description = "Lehnt eine an den angemeldeten Nutzer gerichtete Anfrage ab.")
    @APIResponses({@APIResponse(responseCode = "204", description = "Anfrage wurde erfolgreich abgelehnt"), @APIResponse(responseCode = "403", description = "Für den angemeldeten Nutzer wurde noch kein Profil angelegt"), @APIResponse(responseCode = "404", description = "Anfrage wurde nicht gefunden")})
    public Response decline(@PathParam("id") @Positive Long id) {
        Long me = currentUserId();
        if (me == null) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Profil noch nicht angelegt").build();
        }
        boolean ok = service.decline(id, me);
        if (!ok) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @DELETE
    @Authenticated
    @Path("/{id}")
    @Operation(summary = "Freundschaft entfernen", description = "Löst eine bestehende Freundschaft des angemeldeten Nutzers auf.")
    @APIResponses({@APIResponse(responseCode = "204", description = "Freundschaft wurde erfolgreich entfernt"), @APIResponse(responseCode = "403", description = "Für den angemeldeten Nutzer wurde noch kein Profil angelegt"), @APIResponse(responseCode = "404", description = "Freundschaft wurde nicht gefunden")})
    public Response unfriend(@PathParam("id") @Positive Long id) {
        Long me = currentUserId();
        if (me == null) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Profil noch nicht angelegt").build();
        }
        boolean removed = service.unfriend(id, me);
        if (!removed) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    private Long currentUserId() {
        return service.idByEmail(jwt.getClaim("email"));
    }
}
