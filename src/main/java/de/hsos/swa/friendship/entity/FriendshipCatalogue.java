package de.hsos.swa.friendship.entity;

import java.util.List;
public interface FriendshipCatalogue {
    Long createFriendship(Long requesterId, Long addresseeId);
    Friendship findFriendshipById(Long id);
    Friendship findBetween(Long userA, Long userB);      // either direction, any status
    List<Friendship> findAcceptedFor(Long userId);        // my friends
    List<Friendship> findPendingFor(Long userId);         // requests waiting for ME
    boolean deleteFriendship(Long id);
    void deleteAllInvolving(Long userId);
}
