package de.hsos.swa.user.boundary.dto;

import de.hsos.swa.user.entity.User;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


@Schema(name = "UserResponse", description = "Darstellung eines Nutzerprofils")
public record UserResponseDTO(@Schema(description = "Eindeutige Nutzer-ID", example = "1006") Long id,

                              @Schema(description = "Öffentlicher Anzeigename des Nutzers", example = "Maxxy Cat") String displayName,

                              @Schema(description = "Vorname; nur in der vollständigen Profilansicht enthalten", example = "Max", nullable = true) String firstName,

                              @Schema(description = "Nachname; nur in der vollständigen Profilansicht enthalten", example = "Muster", nullable = true) String lastName,

                              @Schema(description = "Adresse des Nutzers; abhängig von der Sichtberechtigung möglicherweise Straße eingeschränkt", nullable = true) AddressDTO address) {
    public static UserResponseDTO fromEntity(User u) {
        return new UserResponseDTO(u.getId(), u.getDisplayName(), u.getFirstName(), u.getLastName(), u.getAddress() == null ? null : new AddressDTO(u.getAddress().getCity(), u.getAddress().getPlz(), u.getAddress().getStreet()));
    }

    public static UserResponseDTO publicView(User u) {
        return new UserResponseDTO(u.getId(), u.getDisplayName(), null,                                                            // firstName: friends only
                null,                                                                                                                       // lastName: friends only
                u.getAddress() == null ? null : new AddressDTO(u.getAddress().getCity(), u.getAddress().getPlz(), null                     // street: friends only
                ));
    }
}
