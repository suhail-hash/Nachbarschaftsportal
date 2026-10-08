package de.hsos.swa.user.entity;
import java.util.List;
import java.util.Set;

/**
 * Control-Schnittstelle fuer die Verwaltung von Usern.
 * Wird von der Panache-Klasse im Gateway implementiert.
 */

public interface UserCatalogue {
    Long createUser(String email, String displayName,String firstName, String lastName, Address address);
    User findUserById(Long id);
    List<User> getAllUsers();
    User findUserByEmail(String email);
    void deleteUser(Long id);
    List<User> findUsersByIds(Set<Long> ids);
    List<Long> findAllUserIds();
}
