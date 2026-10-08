package de.hsos.swa.friendship.control;

import de.hsos.swa.friendship.entity.Friendship;
import de.hsos.swa.friendship.entity.FriendshipCatalogue;
import de.hsos.swa.friendship.gateway.acl.UserLookup;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


@QuarkusTest
public class FriendshipServiceTest {
    @Inject
    FriendshipService service;

    @InjectMock
    FriendshipCatalogue catalogue;

    @InjectMock
    UserLookup users;

    @Test
    void requestToSelfIsRejected() {
        assertNull(service.sendRequest(1L, 1L));
        verifyNoInteractions(catalogue);
    }

    @Test
    void duplicateRequestIsRejected() {
        when(catalogue.findBetween(1L, 2L)).thenReturn(new Friendship(1L, 2L));

        assertNull(service.sendRequest(1L, 2L));
        verify(catalogue, never()).createFriendship(anyLong(), anyLong());
    }

    @Test
    void validRequestCreatesFriendship() {
        when(catalogue.findBetween(1L, 2L)).thenReturn(null);
        when(users.exists(1L)).thenReturn(true);
        when(users.exists(2L)).thenReturn(true);
        when(catalogue.createFriendship(1L, 2L)).thenReturn(99L);

        assertEquals(99L, service.sendRequest(1L, 2L));
        verify(catalogue).createFriendship(1L, 2L);
    }
}
