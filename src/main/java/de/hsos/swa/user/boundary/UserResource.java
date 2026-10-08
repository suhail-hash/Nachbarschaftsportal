package de.hsos.swa.user.boundary;

import de.hsos.swa.user.boundary.dto.*;
import de.hsos.swa.user.control.UserService;
import de.hsos.swa.user.entity.Address;
import de.hsos.swa.user.entity.User;
import io.quarkus.security.Authenticated;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

//https://quarkus.io/guides/openapi-swaggerui

@Tag(name = "Nutzer", description = "Verwaltung von Nutzerprofilen und Profildaten")
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserResource {
    @Inject
    UserService userService;
    @Inject
    JsonWebToken jwt;

    @Timeout(2000)
    @Authenticated
    @GET
    @Operation(summary = "Alle Nutzer abrufen", description = "Liefert die öffentliche Ansicht aller vorhandenen Nutzer.")
    @APIResponses({@APIResponse(responseCode = "200", description = "Nutzerliste wurde erfolgreich geladen"), @APIResponse(responseCode = "401", description = "Nutzer ist nicht angemeldet")})
    public Response getAll() {
        List<UserResponseDTO> users = userService.getAllUsers().stream().map(UserResponseDTO::publicView).toList();
        return Response.ok(users).build();
    }

    @Retry(maxRetries = 2, delay = 200)
    @Timeout(2000)
    @Fallback(fallbackMethod = "getByIdFallback")
    @Authenticated
    @GET
    @Path("/{id}")
    @Operation(summary = "Nutzerprofil abrufen", description = "Liefert abhängig vom Freundschaftsstatus die öffentliche oder vollständige Profilansicht.")
    @APIResponses({@APIResponse(responseCode = "200", description = "Nutzerprofil wurde erfolgreich geladen"), @APIResponse(responseCode = "401", description = "Nutzer ist nicht angemeldet"), @APIResponse(responseCode = "403", description = "Für den angemeldeten Nutzer wurde noch kein Profil angelegt"), @APIResponse(responseCode = "404", description = "Nutzer wurde nicht gefunden"), @APIResponse(responseCode = "503", description = "Nutzerdaten sind momentan nicht verfügbar")})
    public Response getById(@PathParam("id") @Positive Long id) {
        Long viewer = currentUserId();
        if (viewer == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("Profil noch nicht angelegt").build();
        }
        User user = userService.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        boolean fullView = userService.canSeeFullProfile(viewer, id);
        return Response.ok(fullView ? UserResponseDTO.fromEntity(user) : UserResponseDTO.publicView(user)).build();
    }

    public Response getByIdFallback(@Positive Long id) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Nutzerdaten momentan nicht verfügbar, bitte später erneut versuchen").build();
    }

    @POST
    @Authenticated
    @Operation(summary = "Nutzerprofil anlegen", description = "Erstellt für den angemeldeten Keycloak-Nutzer ein neues Profil.")
    @APIResponses({@APIResponse(responseCode = "201", description = "Nutzerprofil wurde erfolgreich erstellt"), @APIResponse(responseCode = "400", description = "Die übermittelten Profildaten sind ungültig"), @APIResponse(responseCode = "401", description = "Nutzer ist nicht angemeldet"), @APIResponse(responseCode = "409", description = "Für diesen Nutzer existiert bereits ein Profil")})
    public Response create(@Valid CreateUserDTO dto) {
        try {
            Long id = userService.createUser(jwt.getClaim("email"), dto.getDisplayName(), jwt.getClaim("given_name"), jwt.getClaim("family_name"), dto.toAddress());
            return Response.created(URI.create("/users/" + id)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @Authenticated
    @PUT
    @Path("/{id}/address")
    @Operation(summary = "Adresse ändern", description = "Ändert die Adresse des eigenen Nutzerprofils.")
    @APIResponses({@APIResponse(responseCode = "204", description = "Adresse wurde erfolgreich geändert"), @APIResponse(responseCode = "400", description = "Die übermittelte Adresse ist ungültig"), @APIResponse(responseCode = "401", description = "Nutzer ist nicht angemeldet"), @APIResponse(responseCode = "403", description = "Das Profil gehört nicht zum angemeldeten Nutzer"), @APIResponse(responseCode = "404", description = "Nutzer wurde nicht gefunden")})
    public Response changeAddress(@PathParam("id") Long id, @Valid AddressDTO dto) {
        if (!isOwner(id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Address newAddress = dto.toEntity();
        boolean updated = userService.changeAddress(id, newAddress);
        if (!updated) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @Authenticated
    @PUT
    @Path("/{id}/display-name")
    @Operation(summary = "Anzeigenamen ändern", description = "Ändert den öffentlichen Anzeigenamen des eigenen Profils.")
    @APIResponses({@APIResponse(responseCode = "204", description = "Anzeigename wurde erfolgreich geändert"), @APIResponse(responseCode = "400", description = "Der übermittelte Anzeigename ist ungültig"), @APIResponse(responseCode = "401", description = "Nutzer ist nicht angemeldet"), @APIResponse(responseCode = "403", description = "Das Profil gehört nicht zum angemeldeten Nutzer"), @APIResponse(responseCode = "404", description = "Nutzer wurde nicht gefunden")})
    public Response changeDisplayName(@PathParam("id") Long id, @Valid DisplayNameDTO dto) {
        if (!isOwner(id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        boolean updated = userService.changeDisplayName(id, dto.displayName());
        if (!updated) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @Authenticated
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Nutzerkonto löschen", description = "Löscht das eigene Nutzerprofil nach Bestätigung der E-Mail-Adresse.")
    @APIResponses({@APIResponse(responseCode = "204", description = "Nutzerprofil wurde erfolgreich gelöscht"), @APIResponse(responseCode = "400", description = "Die Bestätigungs-E-Mail stimmt nicht überein"), @APIResponse(responseCode = "401", description = "Nutzer ist nicht angemeldet"), @APIResponse(responseCode = "403", description = "Das Profil gehört nicht zum angemeldeten Nutzer"), @APIResponse(responseCode = "404", description = "Nutzer wurde nicht gefunden")})
    public Response deleteAccount(@PathParam("id") Long id, @Valid DeleteConfirmDTO dto) {
        if (!isOwner(id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        String tokenEmail = jwt.getClaim("email");
        if (dto == null || dto.confirmEmail() == null || !dto.confirmEmail().trim().equalsIgnoreCase(tokenEmail)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Bestätigungs-E-Mail stimmt nicht überein").build();
        }
        boolean deleted = userService.deleteAccount(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    //Nutzer werden derzeit über ihre E-Mail-Adresse identifiziert, die zugleich als Keycloak-Benutzername dient. Stabiler wäre die Identifikation über den unveränderlichen Keycloak-Subject-Claim (sub); die E-Mail könnte dann rein als Profildatum geführt werden.
    private boolean isOwner(Long id) {
        Long viewer = currentUserId();
        return viewer != null && viewer.equals(id);
    }

    private Long currentUserId() {
        return userService.idByEmail(jwt.getClaim("email"));
    }

//    private Long currentUserId() {
//        if (securityIdentity.isAnonymous()) {
//            return null;
//        }
//        String email = securityIdentity.getPrincipal().getName();  //getName() = preferred_username bei jwt... was die Email Adresse auch ist!
//        return userService.idByEmail(email);
//    }
}
