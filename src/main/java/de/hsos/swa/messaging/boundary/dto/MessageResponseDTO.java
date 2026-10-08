package de.hsos.swa.messaging.boundary.dto;

import de.hsos.swa.messaging.entity.Message;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Empfangene Nachricht")
public record MessageResponseDTO(
        @Schema(description = "Eindeutige ID der Nachricht", example = "5001", readOnly = true, nullable = false)
        Long id,

        @Schema(description = "ID des Absenders", example = "42", nullable = false)
        Long senderId,

        @Schema(description = "ID des Empfaengers", example = "43", nullable = false)
        Long recipientId,

        @Schema(description = "Nachrichtentext", example = "Hallo", nullable = false)
        String text,

        @Schema(description = "Zeitpunkt des Versands (UTC)", example = "2025-07-19T14:30:00Z", nullable = false)
        Instant sentAt,

        @Schema(description = "ID des verknuepften Angebots; nur gesetzt, wenn die Nachricht aus einem Angebot heraus geschrieben wurde", example = "1001", nullable = true)
        Long offerId
) {
    public static MessageResponseDTO fromDTO(Message message) {
        return new MessageResponseDTO(
                message.getId(),
                message.getSenderId(),
                message.getRecipientId(),
                message.getText(),
                message.getSentAt(),
                message.getOfferId()
        );
    }
}