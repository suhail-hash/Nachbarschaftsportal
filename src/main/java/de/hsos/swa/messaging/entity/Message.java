package de.hsos.swa.messaging.entity;


import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long recipientId;

    @Column(nullable = false, length = 2000)
    private String text;

    @Column(nullable = false)
    private Instant sentAt;

    @Column
    private Long offerId; //Für Anzeigebezug

    protected Message() {}

    public Message(Long senderId, Long recipientId, String text, Long offerId){
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.text = text;
        this.offerId = offerId;
        this.sentAt = Instant.now();
    }

    public long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public String getText() {
        return text;
    }

    public Long getOfferId() {
        return offerId;
    }
    public Instant getSentAt() {
        return sentAt;
    }
}
