package de.hsos.swa.messaging.gateway.acl;

import java.util.Map;
import java.util.Set;

public interface UserLookup {


    boolean exists(Long userId);


    Long idByEmail(String email);


    Map<Long, String> labelsFor(Long viewerId, Set<Long> userIds);


    Set<Long> allUserIds();
}
