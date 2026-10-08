package de.hsos.swa.user.gateway;
import de.hsos.swa.user.entity.UserCatalogue;
import de.hsos.swa.user.entity.Address;
import de.hsos.swa.user.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Gateway-Schnittstelle fuer die User-Persistenz.
 */
@ApplicationScoped
public class UserRepository  implements PanacheRepository<User>, UserCatalogue{
    @Transactional
    @Override
    public Long createUser(String email,String displayName,String firstName, String lastName, Address address) {
        User user = new User(email,displayName,firstName,lastName, address);
        persist(user);
        return user.getId();
    }
    @Override
    public User findUserById(Long id) {
        return findById(id);
    }
    @Override
    public List<User> getAllUsers() {
        return listAll();
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        deleteById(id);
    }

    @Override
    public User findUserByEmail(String email) {
        return find("email", email).firstResult();
    }

    /**
     * Batch-Lookup: alle User zu einer Menge von Ids in EINER Query
     * (WHERE id IN (...)) statt einer Query pro Id (N+1-Vermeidung).
     * Wird vom Label-Aufbau fuer die Inbox genutzt (Messaging Module).
     */
    @Override
    public List<User> findUsersByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return list("id in ?1", ids); //SELECT * FROM User WHERE id IN (1002, 1003, 1004)
    }

    @Override
    public List<Long> findAllUserIds() {
        List<Long> ids = new ArrayList<>();
        for (User u : listAll()) {
            ids.add(u.getId());
        }
        return ids;
    }
}
