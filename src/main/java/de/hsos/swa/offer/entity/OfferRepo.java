package de.hsos.swa.offer.entity;

import java.util.List;
import java.util.Optional;
public interface OfferRepo {

    Long addOffer(Offer Offer);

    Offer updateOffer(Long id, Offer Offer);

    Optional<Offer> getOffer(Long id);

    List<Offer> getOffers();

    List<Offer> getOffersByUserId(Long id);

    List<Offer> getOffersByRadius(double longitude, double latitude, double radius);

    List<Offer> getOffersByRadius(Long userId, double longitude, double latitude, double radius);

    List<Offer> getOffersByCategoryName(Long userId, double longitude, double latitude, OfferCategory category, double radius);

    List<Offer> getOffersByCategorySubjectName(Long userId, double longitude, double latitude, OfferCategory category, OfferCategorySubject categorySubject, double radius);

    boolean deleteOffer(Long id);

    boolean deleteOfferByUserId(Long userId);
}
