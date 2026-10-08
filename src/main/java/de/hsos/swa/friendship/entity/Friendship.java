package de.hsos.swa.friendship.entity;

import jakarta.persistence.*;

import java.time.Instant;
@Entity
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long requesterId;

    @Column(nullable = false)
    private Long addresseeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column
    private Instant respondedAt; // Null at the beginning



    public Friendship(Long requesterId, Long addresseeId) {
        this.requesterId = requesterId;
        this.addresseeId = addresseeId;
        this.status = FriendshipStatus.PENDING;
        this.createdAt = Instant.now();
    }

    protected Friendship() {}

    public boolean isPending() {
        return status == FriendshipStatus.PENDING;
    }

    public FriendshipStatus getStatus() {
        return status;
    }
    public Long getRequesterId() {
        return requesterId;
    }

    public Long getAddresseeId() {
        return addresseeId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getRespondedAt() {
        return respondedAt;
    }

    public Long getId() {
        return id;
    }

    public boolean canBeAnsweredBy(Long callerId) {
        return isPending() && addresseeId.equals(callerId);
    }

    public void accept() {
        this.status = FriendshipStatus.ACCEPTED;
        this.respondedAt = Instant.now();
    }

    public void decline() {
        this.status = FriendshipStatus.DECLINED;
        this.respondedAt = Instant.now();
    }

}
