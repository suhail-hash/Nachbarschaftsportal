package de.hsos.swa.messaging.boundary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Daten zum Senden einer Nachricht")
public record MessageRequestDTO(
        @Schema(description = "ID des Empfängers", example = "42", nullable = false)
        @NotNull(message = "Empfänger darf nicht fehlen")
        Long recipientId,

        @Schema(description = "Nachrichtentext", example = "Hallo", nullable = false)
        @NotBlank(message = "Nachricht darf nicht leer sein")
        String text,

        @Schema(description = "Optionale ID des verknüpften Angebots; nur gesetzt, wenn aus einem Angebot heraus geschrieben wird", example = "1001", nullable = true)
        Long offerId
) {}
