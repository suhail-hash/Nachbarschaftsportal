package de.hsos.swa.offer.gateway.acl;

import java.util.Map;
import java.util.Set;
public interface UserLookup {

    record UserRecord(Long userId, String email, String displayName, String firstName, String lastName, AddressRecord address){}

    record AddressRecord(String city, String plz, String street){}

    UserRecord getUserByUsername(String username);

    Map<Long, String> labelsFor(Long viewerId, Set<Long> userIds);

}
