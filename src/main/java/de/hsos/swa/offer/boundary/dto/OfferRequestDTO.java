package de.hsos.swa.offer.boundary.dto;


import de.hsos.swa.offer.entity.OfferCategory;
import de.hsos.swa.offer.entity.OfferCategorySubject;
public record OfferRequestDTO(
        double longitude,
        double latitude,
        OfferCategory category,
        OfferCategorySubject categorySubject,
        double radius
){}
