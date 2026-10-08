package de.hsos.swa.offer.control;

import de.hsos.swa.offer.entity.*;
import de.hsos.swa.offer.gateway.acl.UserLookup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
@ApplicationScoped
public class OfferServiceImpl implements OfferService {

    @Inject
    OfferRepo offerRepo;

    @Inject
    UserLookup userLookup;

    @Inject
    Logger logger;

    @Override
    public Optional<Offer> getOfferById(long id) {
        return offerRepo.getOffer(id);
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerRepo.getOffers();
    }

    @Override
    public List<Offer> getAllOffersByUserId(Long id) {
        return offerRepo.getOffersByUserId(id);
    }


    @Override
    public List<Offer> getAllOffersByParams(Long userId, double longitude, double latitude, OfferCategory category, OfferCategorySubject categorySubject, double radius) {

        logger.info("UserId: " + userId + " radius: " + radius + " category: " + category + " categorySubject: " + categorySubject + " logitude: " + longitude +  " latitude: " + latitude);

        if(userId != null) {
            if (category == null && categorySubject == null)
            {
                return offerRepo.getOffersByRadius(userId, longitude, latitude, radius);
            }
            else if (category != null && categorySubject == null)
            {
                return offerRepo.getOffersByCategoryName(userId, longitude, latitude, category, radius);
            }
            else
            {
                return offerRepo.getOffersByCategorySubjectName(userId, longitude, latitude, category, categorySubject, radius);
            }
        }
        else
        {
            return offerRepo.getOffersByRadius(longitude, latitude, radius);
        }
    }

    @Override
    public Long createOffer(Offer offer) {
        return offerRepo.addOffer(offer);
    }

    @Override
    public Offer updateOffer(long id, Offer offer) {
       return offerRepo.updateOffer(id, offer);
    }

    @Override
    public boolean deleteOffer(long id) {
        return offerRepo.deleteOffer(id);
    }

    @Override
    public UserLookup.UserRecord getUserByEmail(String email) {
       return userLookup.getUserByUsername(email);
    }

    @Override
    public Map<Long, String> getUserLabes(Long viewerId, Set<Long> userIds){
        return userLookup.labelsFor(viewerId, userIds);
    }
}
