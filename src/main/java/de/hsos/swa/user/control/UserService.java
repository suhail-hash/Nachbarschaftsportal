package de.hsos.swa.user.control;
import de.hsos.swa.shared.events.UserCreated;
import de.hsos.swa.shared.events.UserDeleted;
import de.hsos.swa.user.entity.Address;
import de.hsos.swa.user.entity.User;
import de.hsos.swa.user.entity.UserCatalogue;
import de.hsos.swa.user.gateway.acl.FriendshipLookup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.*;


@ApplicationScoped
public class UserService implements UserDirectory {

    private final Random random = new Random();
    @Inject
    UserCatalogue catalogue;
    @Inject
    FriendshipLookup friends;
    @Inject
    Event<UserCreated> userCreated;
    @Inject
    Event<UserDeleted> userDeleted;

    public Long createUser(String email,String chosenName, String firstName,String lastName, Address address) {
        String displayName = (chosenName != null && !chosenName.isBlank())
                ? chosenName
                : generatePseudonym();
        Long id = catalogue.createUser(email, displayName, firstName, lastName, address);
        userCreated.fire(new UserCreated(id, displayName));
        return id;
    }

    @Transactional
    public boolean changeAddress(Long id, Address newAddress) {
        User user = catalogue.findUserById(id);
        if (user == null) {return false;}
        user.changeLocation(newAddress);
        return true;
    }

    @Transactional
    public boolean changeDisplayName(Long id, String displayName) {
        User user = catalogue.findUserById(id);
        if (user == null) {
            return false;
        }
        user.changeDisplayName(displayName);
        return true;
    }

    public List<User> getAllUsers() {
        return catalogue.getAllUsers();
    }

    public User findById(Long id) {
        return catalogue.findUserById(id);
    }

    @Override
    public boolean exists(Long id) {
        return catalogue.findUserById(id) != null;
    }

    // Duplikate sind ok: displayName ist nur ein Label
    private String generatePseudonym() {
        int number = 1000 + random.nextInt(9000);   // 1000..9999
        return "Nachbar " + number;
    }

    public Long idByEmail(String email) {
        if (email == null) return null;
        User user = catalogue.findUserByEmail(email);
        return user != null ? user.getId() : null;
    }

    /**
     * Sichtbarkeitsregel des User-Moduls: das volle Profil
     * (realName, Strasse) sieht nur der Nutzer selbst oder ein Freund.
     */
    public boolean canSeeFullProfile(Long viewerId, Long profileOwnerId) {
        return viewerId.equals(profileOwnerId)
                || friends.areFriends(viewerId, profileOwnerId);
    }

    /**
     * Batch: Anzeige-Labels fuer mehrere Nutzer aus Sicht EINES Viewers.
     * Freunde sehen "Vorname Nachname (DisplayName)", alle anderen nur
     * das Pseudonym. Die Entscheidung faellt HIER -- Konsumenten
     * (z.B. Messaging) erfahren nie, warum ein Label so aussieht.
     */
    @Override
    public Map<Long, String> labelsFor(Long viewerId, Set<Long> ids) {
        Set<Long> friendIds = friends.friendIdsOf(viewerId);          // 1 Query: meine Freunde
        Map<Long, String> labels = new HashMap<>();
        for (User user : catalogue.findUsersByIds(ids)) {             // 1 Query: die Partner
            boolean friend = friendIds.contains(user.getId());
            labels.put(user.getId(), formatLabel(user, friend));
        }
        return labels;
    }

    /** Einzige Stelle, an der das Label-Format existiert. */
    private String formatLabel(User u, boolean friend) {
        if (friend) {
            return u.getFirstName() + " " + u.getLastName()
                    + " (" + u.getDisplayName() + ")";
        }
        return u.getDisplayName();
    }

    @Override
    public Set<Long> allUserIds() {
        return new HashSet<>(catalogue.findAllUserIds());
    }

    @Override
    public User findUserByEmail(String email) {
        return catalogue.findUserByEmail(email);
    }

    //Transactional hier. Das komplette Flow User->event->friendship
    @Transactional
    public boolean deleteAccount(Long id) {
        if (catalogue.findUserById(id) == null) {
            return false;
        }
        catalogue.deleteUser(id);
        userDeleted.fire(new UserDeleted(id));
        return true;
    }
}
