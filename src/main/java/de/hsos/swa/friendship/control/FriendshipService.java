package de.hsos.swa.friendship.control;

import de.hsos.swa.friendship.entity.Friendship;
import de.hsos.swa.friendship.entity.FriendshipStatus;
import de.hsos.swa.friendship.entity.FriendshipCatalogue;
import de.hsos.swa.friendship.gateway.acl.UserLookup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class FriendshipService implements FriendshipDirectory {

    @Inject
    FriendshipCatalogue catalogue;

    @Inject
    UserLookup users;

    @Transactional
    public Long sendRequest(Long senderId, Long addresseeId) {
        if (senderId == null || addresseeId == null) return null;               //beide Angaben erhalten
        if (senderId.equals(addresseeId)) return null;                          // man keine selbstanfrage
        if (catalogue.findBetween(senderId, addresseeId) != null) return null;  // keine Duplikate
        if (!users.exists(senderId) || !users.exists(addresseeId)) return null; //beide existieren
        return catalogue.createFriendship(senderId, addresseeId);
    }
    @Transactional
    public boolean unfriend(Long friendshipId, Long callerId) {
        Friendship f = catalogue.findFriendshipById(friendshipId);
        if (f == null) {
            return false;
        }
        if (!f.getRequesterId().equals(callerId) && !f.getAddresseeId().equals(callerId)) {
            return false;   // nur Beteiligte duerfen loeschen
        }
        return catalogue.deleteFriendship(friendshipId);
    }

    @Transactional
    public boolean accept(Long friendshipId, Long callerId) {
        Friendship friendship = catalogue.findFriendshipById(friendshipId);
        if (friendship == null || !friendship.canBeAnsweredBy(callerId)) return false;
        friendship.accept();
        return true;
    }

    @Transactional
    public boolean decline(Long friendshipId, Long callerId) {
        Friendship friendship = catalogue.findFriendshipById(friendshipId);
        if (friendship == null || !friendship.canBeAnsweredBy(callerId)) return false;
        friendship.decline();
        return true;
    }
    public List<FriendshipView> friendViewsOf(Long userId) {
        return buildViewsFor(catalogue.findAcceptedFor(userId), userId);
    }
    public List<FriendshipView> requestViewsFor(Long userId) {
        return buildViewsFor(catalogue.findPendingFor(userId), userId);
    }

    public boolean areFriends(Long userA, Long userB) {
        Friendship friendship = catalogue.findBetween(userA, userB);
        return friendship != null && friendship.getStatus() == FriendshipStatus.ACCEPTED;
    }

    @Override
    public Long idByEmail(String email) {
        return users.idByEmail(email);
    }

    // Dieser Vorgang wird vom Claude Opus 4.8 vorgeschlagen, um das Problem von der Namenlabeling im Messaging Module zu Loesen.
    @Override
    public Set<Long> friendIdsOf(Long userId) {
        //all ACCEPTED friendships where I'm on either side
        return catalogue.findAcceptedFor(userId).stream()
                //for each friendship, take the other person's id: if I'm the requester, the friend is the addressee; otherwise the requester
                .map(f -> f.getRequesterId().equals(userId)
                        ? f.getAddresseeId()
                        : f.getRequesterId())
                // pour the ids into a Set (no duplicates)
                .collect(Collectors.toSet());
    }

    private List<FriendshipView> buildViewsFor(List<Friendship> friendships, Long userId) {
        Set<Long> partnerIds = new HashSet<>();
        for (Friendship f : friendships) {
            partnerIds.add(partnerOf(f, userId));
        }
        Map<Long, String> labels = users.labelsFor(userId, partnerIds);

        List<FriendshipView> views = new ArrayList<>();
        for (Friendship f : friendships) {
            Long partnerId = partnerOf(f, userId);
            views.add(new FriendshipView(f.getId(), partnerId, labels.get(partnerId)));
        }
        return views;
    }

    //returns the id of the partener.. if u are the requester then the addressee will be returned, otherwise the requester since u will be the addressee in that case
    private Long partnerOf(Friendship f, Long userId) {
        return f.getRequesterId().equals(userId)
                ? f.getAddresseeId()
                : f.getRequesterId();
    }


}
