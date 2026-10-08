package de.hsos.swa.user.boundary.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record DeleteConfirmDTO(
        @Schema(description = "E-Mail zur Bestätigung; muss der Token-E-Mail entsprechen", example = "max.muster@example.com", nullable = false)
        @NotBlank(message = "Bestätigungs-E-Mail darf nicht leer sein")
        @Email(message = "Bestätigungs-E-Mail ist ungültig")
        String confirmEmail
) {
}
