package de.hsos.swa.user.gateway.acl;

import java.util.Set;

public interface FriendshipLookup {
    boolean areFriends(Long userA, Long userB);
    Set<Long> friendIdsOf(Long userId);
}
