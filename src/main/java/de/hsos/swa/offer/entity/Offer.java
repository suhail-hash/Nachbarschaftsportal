package de.hsos.swa.offer.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
@Entity
@Table(name = "offers")
public class Offer {

    //https://quarkus.io/guides/hibernate-orm-panache
    @Id
    @SequenceGenerator(
            name = "offerSequence",
            allocationSize = 1,
            initialValue = 300)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offerSequence")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferCategorySubject categorySubject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferPrivacy privacy;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private double longitudeCity;

    @Column(nullable = false)
    private double latitudeCity;

    @Column(nullable = false)
    private double longitudeAddress;

    @Column(nullable = false)
    private double latitudeAddress;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = true)
    private String imageName;

    @Column(nullable = true)
    private String addition;

//    @Embedded
//    private Address address;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Offer() {}

    public Offer(String title, String description, OfferCategory category, OfferCategorySubject categorySubject, Long ownerId, OfferPrivacy privacy, double longitudeAddress, double latitudeAddress, double latitudeCity, double longitudeCity, BigDecimal price, String imageName, String addition, OfferStatus status) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.privacy = privacy;
        this.categorySubject = categorySubject;
        this.ownerId = ownerId;
        this.longitudeAddress = longitudeAddress;
        this.latitudeAddress = latitudeAddress;
        this.longitudeCity = longitudeCity;
        this.latitudeCity = latitudeCity;
        this.price = price;
        this.createdAt = Instant.now();
        this.imageName = imageName;
        this.addition = addition;
        this.status = status;
    }



    public void updateDetails(String title, String description, OfferCategory category) {
        if (title != null)       this.title = title;
        if (description != null)  this.description = description;
        if (category != null)     this.category = category;
    }


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    public OfferCategory getCategory() {
        return category;
    }

    public void setCategory(OfferCategory category) {
        this.category = category;
    }

    public OfferCategorySubject getCategorySubject() {
        return categorySubject;
    }

    public void setCategorySubject(OfferCategorySubject categorySubject) {
        this.categorySubject = categorySubject;
    }

    public OfferPrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(OfferPrivacy privacy) {
        this.privacy = privacy;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public double getLongitudeAddress() {
        return longitudeAddress;
    }

    public void setLongitudeAddress(double longitude) {
        this.longitudeAddress = longitude;
    }

    public double getLatitudeAddress() {
        return latitudeAddress;
    }

    public void setLatitudeAddress(double latitude) {
        this.latitudeAddress = latitude;
    }

    public double getLongitudeCity() {
        return longitudeCity;
    }

    public void setLongitudeCity(double longitudeCity) {
        this.longitudeCity = longitudeCity;
    }

    public double getLatitudeCity() {
        return latitudeCity;
    }

    public void setLatitudeCity(double latitudeCity) {
        this.latitudeCity = latitudeCity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }


}
