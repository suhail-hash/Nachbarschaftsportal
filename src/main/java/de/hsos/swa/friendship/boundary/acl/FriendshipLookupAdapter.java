package de.hsos.swa.friendship.boundary.acl;

import de.hsos.swa.friendship.control.FriendshipDirectory;
import de.hsos.swa.user.gateway.acl.FriendshipLookup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Set;

/**
 * Anti-Corruption-Layer: uebersetzt die veroeffentlichte Schnittstelle
 * des Friendship-Moduls (FriendshipDirectory) in den Port des
 * User-Moduls.
 *
 */

@ApplicationScoped
public class FriendshipLookupAdapter implements FriendshipLookup {
    @Inject
    FriendshipDirectory friendships;

    @Override
    public boolean areFriends(Long userA, Long userB) {
        return friendships.areFriends(userA, userB);
    }

    @Override
    public Set<Long> friendIdsOf(Long userId) {
        return friendships.friendIdsOf(userId);
    }
}
