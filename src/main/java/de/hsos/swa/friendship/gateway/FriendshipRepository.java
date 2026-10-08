package de.hsos.swa.friendship.gateway;

import de.hsos.swa.friendship.entity.Friendship;
import de.hsos.swa.friendship.entity.FriendshipCatalogue;
import de.hsos.swa.friendship.entity.FriendshipStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class FriendshipRepository implements PanacheRepository<Friendship>, FriendshipCatalogue {

    @Override
    public Friendship findFriendshipById(Long id) {
        return findById(id);
    }

    @Override
    public List<Friendship> findAcceptedFor(Long userId) {
        return find("(requesterId = ?1 or addresseeId = ?1) and status = ?2",
                userId, FriendshipStatus.ACCEPTED).list();
    }

    @Override
    public List<Friendship> findPendingFor(Long userId) {
        return find("addresseeId = ?1 and status = ?2",
                userId, FriendshipStatus.PENDING).list();
    }

    //Query Parameters: https://quarkus.io/guides/hibernate-orm-panache
    @Override
    public Friendship findBetween(Long userA, Long userB) {
        return find("(requesterId = ?1 and addresseeId = ?2) or (requesterId = ?2 and addresseeId = ?1)",
                userA, userB).firstResult();
    }

    @Transactional
    @Override
    public Long createFriendship(Long requesterId, Long addresseeId){
        Friendship friendship = new Friendship(requesterId, addresseeId);
        persist(friendship);
        return friendship.getId();
    }

    @Transactional
    @Override
    public boolean deleteFriendship(Long friendshipId){
        return deleteById(friendshipId);
    }

    @Transactional
    @Override
    public void deleteAllInvolving(Long userId) {
        delete("requesterId = ?1 or addresseeId = ?1", userId);
    }
}
