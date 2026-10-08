package de.hsos.swa.friendship.control;

/**
 * Kontroll-Ergebnistyp: eine Freundschaft aus Sicht eines Nutzers
 * der jeweils andere ist der Partner.
 *
 */
public record FriendshipView(Long friendshipId, Long partnerId, String partnerLabel) {}
