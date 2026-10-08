package de.hsos.swa.friendship.boundary;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.hasItems;  //das gleiche wie Contains
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is; //equals
//https://qaautomation.expert/2023/10/15/assertion-of-json-in-rest-assured-using-hamcrest/
//https://hamcrest.org/JavaHamcrest/tutorial
//Static Import geht weil die Methoden im Matchers auch statisch sind, deswegen kann man diese direkt nutzen
@QuarkusTest
class FriendshipResourceTest {

    //GET friendships  --> Freundesliste liefert beide Freunde von Tom   Positive
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = @Claim(key = "email", value = "test1@test.de"))
    void myFriendsReturnsBothFriends() {
        given()
                .when().get("/friendships")
                .then().statusCode(200)
                .body("size()", is(2))
                .body("partnerId", hasItems(1007, 1008));
    }

    //GET friendships/requests  --> keine offenen Anfragen für Tom   Positive
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = @Claim(key = "email", value = "test1@test.de"))
    void requestsIsEmptyWhenNoPending() {
        given()
                .when().get("/friendships/requests")
                .then().statusCode(200)
                .body("size()", is(0));
    }

    //PUT friendships/id/accept  --> nicht existente Anfrage kann nicht angenommen werden   Negative
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = @Claim(key = "email", value = "test1@test.de"))
    void acceptNonexistentRequestReturns404() {
        given()
                .when().put("/friendships/999999/accept")
                .then().statusCode(404);
    }

    //https://github.com/quarkusio/quarkus/issues/44824
    //DELETE friendships/id  --> Unbeteiligter darf fremde Freundschaft nicht löschen   Negative
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = @Claim(key = "email", value = "test1@test.de"))
    void nonParticipantCannotDeleteFriendship() {
        given()
                .when().delete("/friendships/2003")
                .then().statusCode(404);
    }

}
