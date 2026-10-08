package de.hsos.swa.user.boundary.dto;

import de.hsos.swa.user.entity.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


public record AddressDTO(
        @Schema(description = "Stadt", example = "Osnabrück", nullable = false)
        @NotBlank(message = "Stadt darf nicht leer sein")
        @Size(max = 100, message = "Stadt darf maximal 100 Zeichen enthalten")
        String city,

        @Schema(description = "Postleitzahl aus fünf Ziffern", example = "49074", nullable = false)
        @NotBlank(message = "Postleitzahl darf nicht leer sein")
        @Pattern(
                regexp = "\\d{5}",
                message = "Postleitzahl muss aus fünf Ziffern bestehen"
        )
        String plz,

        @Schema(description = "Straße samt Hausnummer; optional", example = "Musterstraße 1", nullable = true)
        @Size(max = 150, message = "Straße darf maximal 150 Zeichen enthalten")
        String street
) {

    public Address toEntity() {
        return new Address(city, plz, street);
    }
}