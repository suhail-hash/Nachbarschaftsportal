package de.hsos.swa.user.control;
import de.hsos.swa.user.entity.User;
import java.util.Map;
import java.util.Set;


public interface UserDirectory {
    boolean exists(Long id);
    Long idByEmail(String email);
    /**
     * Anzeige-Labels fuer mehrere Nutzer aus Sicht des Viewers.
     * Friend gating und Format entscheidet das User-Modul.
     */
    Map<Long, String> labelsFor(Long viewerId, Set<Long> userIds);

    //(GEO): SWAP MIT Umkreis-Suche
    Set<Long> allUserIds();

    User findUserByEmail(String email);
}
