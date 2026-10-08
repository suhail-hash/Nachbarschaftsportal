package de.hsos.swa.friendship.control;

import de.hsos.swa.friendship.entity.FriendshipCatalogue;
import de.hsos.swa.shared.events.UserDeleted;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

public class UserDeletedObserver {
    @Inject
    FriendshipCatalogue catalogue;

    public void onUserDeleted(@Observes UserDeleted event) {
        catalogue.deleteAllInvolving(event.userId());
    }
}
