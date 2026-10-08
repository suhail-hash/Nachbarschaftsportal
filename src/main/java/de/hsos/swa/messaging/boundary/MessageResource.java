package de.hsos.swa.messaging.boundary;

import de.hsos.swa.messaging.boundary.dto.MessageRequestDTO;
import de.hsos.swa.messaging.boundary.dto.MessageResponseDTO;
import de.hsos.swa.messaging.boundary.dto.ChatResponseDTO;
import de.hsos.swa.messaging.control.MessageDirectory;
import de.hsos.swa.messaging.entity.Message;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;


@Tag(name = "Nachrichten", description = "Versand von Nachrichten und Abruf von Chats")
@Path("/messages")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class MessageResource {

    @Inject
    MessageDirectory messageDirectory;

    @Inject
    JsonWebToken jwt;

    @POST
    @Authenticated
    @Operation(summary = "Nachricht senden", description = "Sendet eine Nachricht an einen anderen Nutzer, optional mit Angebotsbezug.")
    @APIResponses({@APIResponse(responseCode = "201", description = "Nachricht wurde erfolgreich versendet"), @APIResponse(responseCode = "400", description = "Die übermittelten Nachrichtendaten sind ungültig"), @APIResponse(responseCode = "401", description = "Nutzer ist nicht angemeldet"), @APIResponse(responseCode = "403", description = "Für den angemeldeten Nutzer wurde noch kein Profil angelegt")})
    public Response send(@Valid MessageRequestDTO dto) {
        Long me = currentUserId();
        if (me == null) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Profil noch nicht angelegt").build();
        }
        try {
            Message message = messageDirectory.sendMessage(
                    me, dto.recipientId(), dto.text(), dto.offerId());
            return Response.status(Response.Status.CREATED)
                    .entity(MessageResponseDTO.fromDTO(message))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }


    @GET
    @Path("/chat/{partnerId}")
    @Retry(maxRetries = 2)
    @Fallback(fallbackMethod = "chatFallback")
    @Authenticated
    @Operation(summary = "Chatverlauf abrufen", description = "Liefert den Nachrichtenverlauf zwischen dem angemeldeten Nutzer und einem Chatpartner.")
    @APIResponses({@APIResponse(responseCode = "200", description = "Chatverlauf wurde erfolgreich geladen"), @APIResponse(responseCode = "401", description = "Nutzer ist nicht angemeldet"), @APIResponse(responseCode = "403", description = "Für den angemeldeten Nutzer wurde noch kein Profil angelegt"), @APIResponse(responseCode = "503", description = "Der Chat ist momentan nicht verfügbar")})
    public Response chat(@PathParam("partnerId") Long partnerId) {
        Long me = currentUserId();
        if (me == null) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Profil noch nicht angelegt").build();
        }
        List<MessageResponseDTO> list = messageDirectory.getChat(me, partnerId).stream()
                .map(MessageResponseDTO::fromDTO)
                .toList();
        return Response.ok(list).build();
    }

    private Response chatFallback(Long partnerId) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Der Chat ist momentan nicht verfügbar")
                .build();
    }


    @GET
    @Path("/chats")
    @Retry(maxRetries = 2)
    @Fallback(fallbackMethod = "chatsFallback")
    @Authenticated
    @Operation(summary = "Chatübersicht abrufen", description = "Liefert alle Chatpartner des angemeldeten Nutzers.")
    @APIResponses({@APIResponse(responseCode = "200", description = "Chatübersicht wurde erfolgreich geladen"), @APIResponse(responseCode = "401", description = "Nutzer ist nicht angemeldet"), @APIResponse(responseCode = "403", description = "Für den angemeldeten Nutzer wurde noch kein Profil angelegt"), @APIResponse(responseCode = "503", description = "Die Chatübersicht ist momentan nicht verfügbar")})
    public Response chats() {
        Long me = currentUserId();
        if (me == null) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Profil noch nicht angelegt").build();
        }
        List<ChatResponseDTO> list = messageDirectory.getChatPartners(me).stream()
                .map(p -> new ChatResponseDTO(p.userId(), p.labelName()))
                .toList();
        return Response.ok(list).build();
    }

    private Response chatsFallback() {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Die Chatübersicht ist momentan nicht verfügbar")
                .build();
    }


    private Long currentUserId() {
        return messageDirectory.idByEmail(jwt.getClaim("email"));
    }
}

//https://quarkus.io/guides/smallrye-fault-tolerance