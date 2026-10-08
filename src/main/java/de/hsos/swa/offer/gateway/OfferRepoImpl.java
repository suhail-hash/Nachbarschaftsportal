package de.hsos.swa.offer.gateway;

import de.hsos.swa.offer.entity.*;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;
@ApplicationScoped
public class OfferRepoImpl implements OfferRepo, PanacheRepository<Offer> {

    @Inject
    Logger logger;

    private final EntityManager em = getEntityManager();

    @Override
    public Long addOffer(Offer Offer) {
        persist(Offer);
        return Offer.getId();
    }

    @Override
    public Optional<Offer> getOffer(Long id) {
        return findByIdOptional(id);
    }

    @Override
    public List<Offer> getOffers() {
        return listAll();
    }

    @Override
    public List<Offer> getOffersByUserId(Long id) {
        List<Offer> offers = list("ownerId", Sort.by("category").descending(), id);
        logger.info("Offers: " + offers.toString());
        if(offers.isEmpty()) {
            return List.of();
        }
        else {
            return offers;
        }
    }

    @Override
    public List<Offer> getOffersByRadius(double longitude, double latitude, double radius) {

        //logger.info("getOffersByRadius: " + longitude + "/" + latitude + " - radius: " + radius);
        //https://stackoverflow.com/questions/76532735/hibernate-native-query-with-panache-repository-in-reactive-quarkus-with-mutiny -> native Query mit PanacheRepo
        return (List<Offer>) em.createNativeQuery(
                        "SELECT * FROM offers o WHERE ST_DWithin(ST_SetSRID(ST_MakePoint(o.longitudeCity, o.latitudeCity), 4326)::geography, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography, :radius) AND o.status = 'AVAILABLE'",
                        Offer.class)
                .setParameter("lat", latitude)
                .setParameter("lon", longitude)
                .setParameter("radius", radius * 1000)
                .getResultList();
    }

    @Override
    public List<Offer> getOffersByRadius(Long userId, double longitude, double latitude, double radius) {

        //logger.info("getOffersByRadius: " + longitude + "/" + latitude + " - radius: " + radius);
        //https://stackoverflow.com/questions/76532735/hibernate-native-query-with-panache-repository-in-reactive-quarkus-with-mutiny -> native Query mit PanacheRepo
        return (List<Offer>) em.createNativeQuery(
                        "SELECT * FROM offers o WHERE ST_DWithin(ST_SetSRID(ST_MakePoint(o.longitudeCity, o.latitudeCity), 4326)::geography, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography, :radius) AND o.ownerId <> :userId AND o.status = 'AVAILABLE'",
                        Offer.class)
                .setParameter("lat", latitude)
                .setParameter("lon", longitude)
                .setParameter("radius", radius * 1000)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Offer> getOffersByCategoryName(Long userId, double longitude, double latitude, OfferCategory category, double radius) {

        return (List<Offer>) em.createNativeQuery(
                        "SELECT * FROM offers o ST_DWithin(ST_SetSRID(ST_MakePoint(o.longitudeCity, o.latitudeCity), 4326)::geography, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography, :radius) AND o.category = :category AND o.ownerId <> :userId AND o.status = 'AVAILABLE'",
                        Offer.class)
                .setParameter("lat", latitude)
                .setParameter("lon", longitude)
                .setParameter("radius", radius * 1000)
                .setParameter("category", category.name())
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Offer> getOffersByCategorySubjectName(Long userId, double longitude, double latitude, OfferCategory category, OfferCategorySubject categorySubject, double radius) {


        List<Integer> distance = em.createNativeQuery(
                        "SELECT ST_Distance(ST_SetSRID(ST_MakePoint(o.longitudeCity, o.latitudeCity), 4326)::geography, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography) " +
                                "FROM offers o ORDER BY ST_Distance(ST_SetSRID(ST_MakePoint(o.longitudeCity, o.latitudeCity), 4326)::geography, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography) ASC")
                .setParameter("lat", latitude)
                .setParameter("lon", longitude).getResultList();

        logger.info("distance : " + distance);

        return (List<Offer>) em.createNativeQuery(
                        "SELECT * FROM offers o WHERE ST_DWithin(ST_SetSRID(ST_MakePoint(o.longitudeCity, o.latitudeCity), 4326)::geography, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography, :radius) AND o.category = :category AND o.categorySubject = :categorySubject AND o.ownerId <> :userId AND o.status = 'AVAILABLE'",
                        Offer.class)
                .setParameter("lat", latitude)
                .setParameter("lon", longitude)
                .setParameter("radius", radius * 1000)
                .setParameter("category", category.name())
                .setParameter("categorySubject", categorySubject.name())
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Offer updateOffer(Long id, Offer data) {

        Optional<Offer> existing = getOffer(id);

        if (existing.isEmpty()) {
            throw new NotFoundException();
        }

        Offer offer = existing.get();

        offer.setTitle(data.getTitle());
        offer.setDescription(data.getDescription());
        offer.setCategory(data.getCategory());
        offer.setCategorySubject(data.getCategorySubject());
        offer.setPrivacy(data.getPrivacy());
        offer.setLongitudeAddress(data.getLongitudeAddress());
        offer.setLatitudeAddress(data.getLatitudeAddress());
        offer.setLatitudeCity(data.getLatitudeCity());
        offer.setLongitudeCity(data.getLongitudeCity());
        offer.setPrice(data.getPrice());
        offer.setAddition(data.getAddition());
        offer.setImageName(data.getImageName());
        offer.setStatus(data.getStatus());

        return offer;
    }

    @Override
    public boolean deleteOffer(Long id) {
        return deleteById(id);
    }

    @Override
    public boolean deleteOfferByUserId(Long userId) {
        long deletedCount = delete("ownerId", userId);
        return deletedCount > 0;
    }



    /*@Override
    public List<Offer> getOffersByStatus(String status) {
        try {
            OfferStatus statusEnum = OfferStatus.valueOf(status);
            return list("OfferStatus", statusEnum);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }*/
}
