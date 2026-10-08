package de.hsos.swa.messaging.control;

/**
 * Kontroll-Ergebnistyp: ein Gespraechspartner mit Anzeigenamen.
 */
public record ChatPartner(Long userId, String labelName) { }
