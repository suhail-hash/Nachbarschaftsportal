package de.hsos.swa.messaging.control;

import de.hsos.swa.messaging.entity.Message;
import de.hsos.swa.messaging.entity.MessageCatalogue;
import de.hsos.swa.messaging.gateway.acl.UserLookup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class MessageService implements MessageDirectory{

    @Inject
    MessageCatalogue catalogue;

    @Inject
    UserLookup users;

    @Override
    @Transactional
    public Message sendMessage(Long senderId, Long recipientId, String text, Long offerId) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Nachricht darf nicht leer sein");
        }
        if (senderId.equals(recipientId)) {
            throw new IllegalArgumentException("Nachricht an sich selbst nicht möglich");
        }
        if (!users.exists(recipientId)) {
            throw new IllegalArgumentException("Empfänger existiert nicht");
        }
        Message message = new Message(senderId, recipientId, text, offerId);
        catalogue.save(message);
        return message;
    }

    @Override
    public List<Message> getChat(Long userId, Long partnerId) {
        return catalogue.findChat(userId, partnerId);
    }

    /**
     * Alle Gespraechspartner des Nutzers, mit Anzeigenamen angereichert.
     */
    @Override
    public List<ChatPartner> getChatPartners(Long userId) {
        Set<Long> partnerIds = catalogue.findPartnerIds(userId);
        Map<Long, String> labels = users.labelsFor(userId, partnerIds);
        List<ChatPartner> partners = new ArrayList<>();
        for (Long id : partnerIds) {
            String label = labels.get(id);
            if (label == null) {
                label = "Unbekannter Nutzer";   //can be used later in case a user got deleted...
            }
            partners.add(new ChatPartner(id, label));
        }
        return partners;
    }

    @Override
    @Transactional
    public void sendWelcomeBroadcast(Long newUserId, String displayName) {
        String text = "Hallo! Ich bin " + displayName
                + " und neu im Nachbarschaftsportal. ";
        for (Long recipientId : users.allUserIds()) {
            if (recipientId.equals(newUserId)) {
                continue;
            }
            catalogue.save(new Message(newUserId, recipientId, text, null));
        }
    }

    @Override
    public Long idByEmail(String email) {
        return users.idByEmail(email);
    }
}
