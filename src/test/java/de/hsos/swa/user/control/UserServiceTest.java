package de.hsos.swa.user.control;
import de.hsos.swa.user.entity.Address;
import de.hsos.swa.user.entity.User;
import de.hsos.swa.user.entity.UserCatalogue;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@QuarkusTest
public class UserServiceTest {

    @Inject
    UserService service;

    @InjectMock
    UserCatalogue catalogue;

    //changeAddress --> Adresse eines vorhandenen Nutzers wird geändert   Positive
    @Test
    void changeAddressUpdatesExistingUser() {
        //User ist nur eine Entity und kann nicht injected werden
        User user = mock(User.class);
        Address newAddress = new Address(
                "Osnabrück",
                "49080",
                "Lotter Straße 45"
        );
        when(catalogue.findUserById(1006L)).thenReturn(user);
        assertTrue(service.changeAddress(1006L, newAddress));
        verify(user).changeLocation(newAddress);
    }

    //changeDisplayName --> Nicht vorhandener Nutzer wird nicht geändert   Negativ
    @Test
    void changeDisplayNameReturnsFalseWhenUserDoesNotExist() {
        when(catalogue.findUserById(9999L)).thenReturn(null);
        assertFalse(service.changeDisplayName(9999L, "NeuerName"));
        verify(catalogue).findUserById(9999L);
    }
}