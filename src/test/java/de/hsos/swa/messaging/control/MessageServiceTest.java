package de.hsos.swa.messaging.control;

import de.hsos.swa.messaging.entity.Message;
import de.hsos.swa.messaging.entity.MessageCatalogue;
import de.hsos.swa.messaging.gateway.acl.UserLookup;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
public class MessageServiceTest {

    @Inject
    MessageService service;

    @InjectMock
    MessageCatalogue catalogue;

    @InjectMock
    UserLookup users;

    //sendMessage --> Nachricht an sich selbst wird abgelehnt   Negativ
    @Test
    void messageToSelfIsRejected() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.sendMessage(1006L, 1006L, "Hallo", null)
        );

        assertEquals(
                "Nachricht an sich selbst nicht möglich",
                exception.getMessage()
        );

        verifyNoInteractions(users, catalogue);
    }

    //sendMessage --> Gültige Nachricht wird gespeichert   Positive
    @Test
    void validMessageIsSaved() {
        when(users.exists(1007L)).thenReturn(true);

        Message result = service.sendMessage(
                1006L,
                1007L,
                "Hallo Lisa",
                null
        );

        assertEquals(1006L, result.getSenderId());
        assertEquals(1007L, result.getRecipientId());
        assertEquals("Hallo Lisa", result.getText());

        verify(users).exists(1007L);
        verify(catalogue).save(result);
    }
}