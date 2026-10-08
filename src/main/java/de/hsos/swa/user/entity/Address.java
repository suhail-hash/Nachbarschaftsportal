package de.hsos.swa.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


@Embeddable
public class Address {
    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String plz;

    @Column
    private String street;


    protected Address() {}

    public Address(String city, String plz, String street) {
        this.city = city;
        this.plz = plz;
        this.street = street;
    }

    public String getCity() { return city; }
    public String getPlz() { return plz; }
    public String getStreet() { return street; }
}
