package de.hsos.swa.messaging.control;

import de.hsos.swa.messaging.entity.Message;

import java.util.List;

public interface MessageDirectory {

    Message sendMessage(Long senderId, Long recipientId, String text, Long offerId);
    List<ChatPartner> getChatPartners(Long userId);
    void sendWelcomeBroadcast(Long newUserId, String displayName);
    Long idByEmail(String email);
    List<Message> getChat(Long userId, Long partnerId);
}
