package de.hsos.swa.friendship.boundary.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


//https://hibernate.org/validator/
public record FriendRequestDTO(
        @Schema(description = "ID des Nutzers, an den die Anfrage geht", example = "42", nullable = false)
        @NotNull(message = "Empfänger darf nicht fehlen")
        @Positive(message = "Empfänger-ID muss positiv sein")
        Long addresseeId
) {
}
