package de.hsos.swa.user.boundary.dto;
import de.hsos.swa.user.entity.Address;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Eingehende Daten der ProfileSetup-Seite.
 * Identitaetsdaten (E-Mail, Vor-/Nachname) kommen NICHT aus dem Body,
 * sondern aus dem validierten Token.
 */

public class CreateUserDTO {
    @JsonbProperty("displayName")
    @Schema(description = "Öffentlicher Anzeigename, 2–50 Zeichen", example = "MaxMuster", nullable = false)
    @NotBlank(message = "Anzeigename darf nicht leer sein")
    @Size(
            min = 2,
            max = 50,
            message = "Anzeigename muss zwischen 2 und 50 Zeichen lang sein"
    )
    private String displayName;

    @JsonbProperty("address")
    @Schema(description = "Adresse des Nutzers", nullable = false)
    @NotNull(message = "Adresse darf nicht fehlen")
    @Valid //adresse wird in AddressDTO erst validiert
    private AddressDTO address;

    public CreateUserDTO() {}

    public String getDisplayName() { return displayName; }

    public AddressDTO getAddress() { return address; }

    public Address toAddress() {
        return new Address(address.city(), address.plz(), address.street());
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}
