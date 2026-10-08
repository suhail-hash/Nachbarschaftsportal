package de.hsos.swa.user.boundary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record DisplayNameDTO(
        @Schema(description = "Öffentlicher Anzeigename, 2–50 Zeichen", example = "MaxMuster", nullable = false)
        @NotBlank(message = "Anzeigename darf nicht leer sein")
        @Size(
                min = 2,
                max = 50,
                message = "Anzeigename muss zwischen 2 und 50 Zeichen lang sein"
        )
        String displayName
) {
}
