package de.hsos.swa.messaging.gateway;

import de.hsos.swa.messaging.entity.MessageCatalogue;
import de.hsos.swa.messaging.entity.Message;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class MessageRepository implements PanacheRepository<Message>, MessageCatalogue {

    //Queries: https://quarkus.io/guides/hibernate-orm-panache

    @Override
    public List<Message> findChat(Long userA, Long userB){
        return list(
                "(senderId = ?1 and recipientId = ?2) or (senderId = ?2 and recipientId = ?1)",
                Sort.by("sentAt"),
                userA, userB
        );
    }

    // Um alle Chats zu mit den Users zu finden -> ist aber Teuer... Man kann Ein Case Projection Abfrage machen aber ist komplexer....
//    @Override
//    public Set<Long> findPartnerIds(Long userId) {
//        List<Message> messages = list("senderId = ?1 or recipientId = ?1", userId);
//        Set<Long> partners = new HashSet<>();
//        for (Message m : messages) {
//            partners.add(m.getSenderId().equals(userId) ? m.getRecipientId() : m.getSenderId());
//        }
//        return partners;
//    }
    /**
     * Alle Chat-Partner-Ids in EINER Query: die DB nimmt pro Nachricht
     * "die andere Seite" (case) und dedupliziert (distinct) -- statt
     * alle Nachrichten samt Text zu laden und in Java zu filtern.
     *
     *
     * Für jede Nachricht, an der ich beteiligt bin: nimm die andere Id (case), dedupliziere (distinct),
     * gib nur die Ids zurück statt vorher alle Nachrichtenzeilen inklusive 2000-Zeichen-Text zu laden, nur um eine Handvoll Ids zu extrahieren.
     */
    //Claude Vorschlag
    @Override
    public Set<Long> findPartnerIds(Long userId) {
        List<Long> ids = getEntityManager().createQuery( //
                        "select distinct case when m.senderId = :id then m.recipientId else m.senderId end " + //distinct to avoid duplicates, and case for both cases if im sender give me reciepent otherwise sender
                                "from Message m where m.senderId = :id or m.recipientId = :id", Long.class)
                .setParameter("id", userId)
                .getResultList();
        return new HashSet<>(ids);
    }

    @Override
    public void save(Message message) {
        persist(message);
    }
}
