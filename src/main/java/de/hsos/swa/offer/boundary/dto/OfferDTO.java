package de.hsos.swa.offer.boundary.dto;

import de.hsos.swa.offer.entity.*;

import java.math.BigDecimal;
public record OfferDTO(Long id, String title, String description, OfferCategory category, OfferCategorySubject categorySubject, Long ownerId, OfferPrivacy privacy, double longitudeAddress, double latitudeAddress, double latitudeCity, double longitudeCity, BigDecimal price, String imageName, String addition, OfferStatus status) {

    public static OfferDTO fromEntity(Offer offer){
        return new OfferDTO(
                offer.getId(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getCategory(),
                offer.getCategorySubject(),
                offer.getOwnerId(),
                offer.getPrivacy(),
                offer.getLongitudeAddress(),
                offer.getLatitudeAddress(),
                offer.getLongitudeCity(),
                offer.getLatitudeCity(),
                offer.getPrice(),
                offer.getImageName(),
                offer.getAddition(),
                offer.getStatus()
        );
    }

    public Offer toEntity() {
        return new Offer(title, description, category, categorySubject, ownerId, privacy, longitudeAddress, latitudeAddress, latitudeCity, longitudeCity, price, imageName, addition, status);
    }
}
