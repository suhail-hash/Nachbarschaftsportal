package de.hsos.swa.messaging.boundary.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Ein Chatpartner in der Chatuebersicht")
public record ChatResponseDTO(
        @Schema(description = "ID des Chatpartners", example = "43", nullable = false)
        Long userId,

        @Schema(description = "Anzeigename für Fremde oder Anzeige und reale Name für Freunde des Chatpartners", example = "MaxMuster oder Max Muster(MaxMuster)", nullable = false)
        String labelName
) {}
