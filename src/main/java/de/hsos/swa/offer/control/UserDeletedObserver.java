package de.hsos.swa.offer.control;


import de.hsos.swa.offer.entity.OfferRepo;
import de.hsos.swa.shared.events.UserDeleted;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
public class UserDeletedObserver {
    @Inject
    OfferRepo offerRepo;

    public void onUserDeleted(@Observes UserDeleted event) {
        offerRepo.deleteOfferByUserId(event.userId());
    }
}
