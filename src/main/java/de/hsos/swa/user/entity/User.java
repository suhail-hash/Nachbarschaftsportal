package de.hsos.swa.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    //vom Keycloak
    @Column(nullable = false, unique = true)
    private String email;

    // Pseudonym / Handle
    @Column(nullable = false)
    private String displayName;

    @Column (nullable = false)
    private String firstName;

    @Column (nullable = false)
    private String lastName;

    @Embedded
    private Address address;

    protected User() {
        // Deserialisierung
    }

    public User(String email, String displayName, String firstName, String lastName, Address address) {
        this.email = email;
        this.displayName = displayName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    // --- Getter ---
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Address getAddress() { return address; }

    public void changeDisplayName(String displayName) {
        if (displayName != null && !displayName.isBlank()) {
            this.displayName = displayName;
        }
    }

    // Wohnort aendern
    public void changeLocation(Address newAddress) {
        if (newAddress != null) {
            this.address = newAddress;
        }
    }
}
