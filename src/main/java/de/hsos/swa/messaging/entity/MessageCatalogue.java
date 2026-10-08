package de.hsos.swa.messaging.entity;

import java.util.List;
import java.util.Set;

public interface MessageCatalogue {
    List<Message> findChat(Long userA, Long userB);
    Set<Long> findPartnerIds(Long userId);
    void save(Message message);
}
