package de.hsos.swa.friendship.boundary.dto;

import de.hsos.swa.friendship.control.FriendshipView;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record FriendshipViewDTO(
        @Schema(description = "Eindeutige ID der Freundschaft", example = "7001", readOnly = true, nullable = false)
        Long friendshipId,
        @Schema(description = "ID des Freundes bzw. Anfragepartners", example = "43", nullable = false)
        Long partnerId,
        @Schema(description = "Anzeigename des Partners", example = "MaxMuster oder Max Muster(MaxMuster)", nullable = false)
        String partnerLabel
) {
    public static FriendshipViewDTO from(FriendshipView v) {
        return new FriendshipViewDTO(v.friendshipId(), v.partnerId(), v.partnerLabel());
    }
}