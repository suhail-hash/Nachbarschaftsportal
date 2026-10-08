package de.hsos.swa.offer.boundary;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class OfferResourceTest {

    //GET offers/{id} --> Vorhandenes Angebot wird zurückgegeben   Positive
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = {
            @Claim(key = "email", value = "test1@test.de"),
            @Claim(key = "preferred_username", value = "test1@test.de")
    })
    void getOfferByIdReturnsOffer() {
        given()
                .when()
                .get("/offers/100")
                .then()
                .statusCode(200)
                .body("id", is(100))
                .body("title", is("Renault Twingo"))
                .body("category", is("FOR_SALE"))
                .body("ownerId", is(1234));
    }

    //GET offers/{id} --> Nicht vorhandenes Angebot liefert 404   Negativ
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = {
            @Claim(key = "email", value = "test1@test.de"),
            @Claim(key = "preferred_username", value = "test1@test.de")
    })
    void getOfferByIdReturnsNotFound() {
        given()
                .when()
                .get("/offers/9999")
                .then()
                .statusCode(404);
    }


    //PATCH offers/{id} --> Vorhandenes Angebot wird aktualisiert   Positive
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = {
            @Claim(key = "email", value = "test1@test.de"),
            @Claim(key = "preferred_username", value = "test1@test.de")
    })
    void updateOfferUpdatesExistingOffer() {
        given()
                .contentType("application/json")
                .body("""
                    {
                      "id": 215,
                      "title": "Bürostuhl aktualisiert",
                      "description": "Aktualisierte Beschreibung.",
                      "category": "FOR_SALE",
                      "categorySubject": "HOME_AND_GARDEN",
                      "ownerId": 1008,
                      "privacy": "PUBLIC",
                      "longitudeAddress": 8.020,
                      "latitudeAddress": 52.284,
                      "latitudeCity": 52.284,
                      "longitudeCity": 8.023,
                      "price": 65.00,
                      "imageName": "38ca94a2-9015-43d7-a50d-527e53de8f52.png",
                      "addition": "VB",
                      "status": "AVAILABLE"
                    }
                    """)
                .when()
                .patch("/offers/215")
                .then()
                .statusCode(200)
                .body("id", is(215))
                .body("title", is("Bürostuhl aktualisiert"));
    }
}