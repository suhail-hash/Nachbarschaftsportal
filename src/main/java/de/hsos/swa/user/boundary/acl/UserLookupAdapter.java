package de.hsos.swa.user.boundary.acl;

import de.hsos.swa.offer.gateway.acl.UserLookup;
import de.hsos.swa.user.control.UserDirectory;
import de.hsos.swa.user.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class UserLookupAdapter implements UserLookup, de.hsos.swa.messaging.gateway.acl.UserLookup, de.hsos.swa.friendship.gateway.acl.UserLookup {

    @Inject
    UserDirectory userDirectory;

    @Inject
    Logger logger;

    @Override
    public UserRecord getUserByUsername(String username) {
        User user = userDirectory.findUserByEmail(username);

        if(user != null) {
            logger.info("User " + username + " found");
            return new UserRecord(user.getId(), user.getEmail(), user.getDisplayName(), user.getFirstName(), user.getLastName(), new AddressRecord(user.getAddress().getCity(), user.getAddress().getPlz(), user.getAddress().getStreet()));

        }
        else{
            logger.info("User " + username + " not found!");
            return null;
        }
    }

    @Override
    public boolean exists(Long userId) {
        return userDirectory.exists(userId);
    }

    @Override
    public Long idByEmail(String email) {
        return userDirectory.idByEmail(email);
    }

    @Override
    public Map<Long, String> labelsFor(Long viewerId, Set<Long> userIds) {
        return userDirectory.labelsFor(viewerId, userIds);
    }

    @Override
    public Set<Long> allUserIds() {
        return userDirectory.allUserIds();
    }
}
