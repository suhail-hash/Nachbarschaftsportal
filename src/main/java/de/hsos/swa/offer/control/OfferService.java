package de.hsos.swa.offer.control;

import de.hsos.swa.offer.entity.Offer;
import de.hsos.swa.offer.entity.OfferCategory;
import de.hsos.swa.offer.entity.OfferCategorySubject;
import de.hsos.swa.offer.entity.OfferStatus;
import de.hsos.swa.offer.gateway.acl.UserLookup;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
public interface OfferService {


    Optional<Offer> getOfferById(long id);

    List<Offer> getAllOffers();//nur Admin!

    List<Offer> getAllOffersByUserId(Long id);

    List<Offer> getAllOffersByParams(Long userId, double longitude, double latitude,  OfferCategory category, OfferCategorySubject categorySubject, double radius);

    Long createOffer(Offer offer);

    Offer updateOffer(long id, Offer offer);

    boolean deleteOffer(long id);

    UserLookup.UserRecord getUserByEmail(String email);

    Map<Long, String> getUserLabes(Long viewerId, Set<Long> userIds);
}
