package de.hsos.swa.user.boundary;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static io.restassured.RestAssured.given;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import org.junit.jupiter.api.Test;



@QuarkusTest
public class UserResourceTest {

    //GET

    // getById: unbekannte Id -> 404 (Fallback greift NICHT, da Response zurückgegeben, nicht geworfen)
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = @Claim(key = "email", value = "test1@test.de"))
    void getByIdReturnsProfile() {
        given().when().get("/users/1006")
                .then().statusCode(200);
    }

    // fremdes Profil darf nicht abgefragt werden
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = @Claim(key = "email", value = "test1@test.de"))
    void getByUnknownIdReturns404() {
        given().when().get("/users/999999")
                .then().statusCode(404);
    }


    //POST --> neues user Anlegen
    @Test
    @TestTransaction //damit ein DB-Rollback geführt wird nachdem ausführen
    @TestSecurity(user = "neu1@test.de")
    @OidcSecurity(claims = {
            @Claim(key = "email", value = "neu1@test.de"),
            @Claim(key = "given_name", value = "Neu"),
            @Claim(key = "family_name", value = "Nutzer")
    })
    void createNewUserReturns201() {
        given().contentType("application/json")
                .body("{\"displayName\":\"Neuling\",\"address\":{\"city\":\"Osnabrück\",\"plz\":\"49076\",\"street\":\"Teststraße 1\"}}")
                .when().post("/users")
                .then().statusCode(201);
    }

    //PUT users/id/address   --> fremde Adresse darf nicht geändert werden  Negative
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = @Claim(key = "email", value = "test1@test.de"))
    void cannotChangeOtherUsersAddress() {
        given().contentType("application/json")
                .body("{\"city\":\"Hamburg\",\"plz\":\"20095\",\"street\":\"Fake 1\"}")
                .when().put("/users/1007/address")
                .then().statusCode(403);
    }
    @Test
    @TestTransaction
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = @Claim(key = "email", value = "test1@test.de"))
    void canChangeOwnAddress() {
        given().contentType("application/json")
                .body("{\"city\":\"Hamburg\",\"plz\":\"20095\",\"street\":\"Neue Str 1\"}")
                .when().put("/users/1006/address")
                .then().statusCode(204);
    }

    //PUT users/id/display-name  --> fremder Anzeigename darf nicht geändert werden   Negative
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = @Claim(key = "email", value = "test1@test.de"))
    void cannotChangeOtherUsersDisplayName() {
        given().contentType("application/json")
                .body("{\"displayName\":\"Hacker\"}")
                .when().put("/users/1007/display-name")
                .then().statusCode(403);
    }

    //DELETE users/id  --> Löschung mit falscher Bestätigungs-E-Mail wird abgelehnt   Negative
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = @Claim(key = "email", value = "test1@test.de"))
    void deleteWithWrongConfirmEmailIsRejected() {
        given().contentType("application/json")
                .body("{\"confirmEmail\":\"falsch@test.de\"}")
                .when().delete("/users/1006")
                .then().statusCode(400);
    }
}
