package de.hsos.swa.friendship.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FriendshipTest {

    @Test
    void onlyAddresseeCanAnswerPendingRequest() {
        Friendship f = new Friendship(1L, 2L);

        assertTrue(f.canBeAnsweredBy(2L));
        assertFalse(f.canBeAnsweredBy(1L));
    }

    @Test
    void acceptedRequestCanNoLongerBeAnswered() {
        Friendship f = new Friendship(1L, 2L);
        f.accept();

        assertFalse(f.canBeAnsweredBy(2L));
    }
}