package de.hsos.swa.friendship.control;

import java.util.Set;


public interface FriendshipDirectory {
    boolean areFriends(Long userA, Long userB);
    //Ids aller Freunde (ACCEPTED) dieses Nutzers fuer Batch-Sichtbarkeitsentscheidungen
    //Statt pro Partner eine Query (N+1) wird eine Batch-Anfrage gestellt: eine Query mit id IN (...) für die Namen, eine für die Freundesliste des Viewers — konstant vier Queries pro Inbox-Aufruf, unabhängig von der Partnerzahl.
    Set<Long> friendIdsOf(Long userId);
    Long idByEmail(String email);
}