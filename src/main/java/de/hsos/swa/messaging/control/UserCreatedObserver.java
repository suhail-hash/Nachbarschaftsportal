package de.hsos.swa.messaging.control;

import de.hsos.swa.shared.events.UserCreated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserCreatedObserver {
    @Inject
    MessageService messageService;

    public void onUserCreated(@Observes UserCreated event) {
        messageService.sendWelcomeBroadcast(event.userId(), event.displayName());
    }
}
