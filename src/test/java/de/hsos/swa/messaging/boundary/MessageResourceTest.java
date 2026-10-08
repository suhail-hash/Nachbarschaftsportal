package de.hsos.swa.messaging.boundary;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class MessageResourceTest {
    //GET messages/chat/{partnerId} --> Chat mit Lisa liefert beide Nachrichten   Positive
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = {
            @Claim(key = "email", value = "test1@test.de"),
            @Claim(key = "preferred_username", value = "test1@test.de")
    })
    void chatReturnsMessagesWithPartner() {
        given()
                .when()
                .get("/messages/chat/1007")
                .then()
                .statusCode(200)
                .body("size()", is(2));
    }

    //GET messages/chat/{partnerId} --> Tom erhält keine Nachrichten fremder Nutzer   Negativ
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = {
            @Claim(key = "email", value = "test1@test.de"),
            @Claim(key = "preferred_username", value = "test1@test.de")
    })
    //TODO
    void chatDoesNotExposeMessagesBetweenOtherUsers() {
        given()
                .when()
                .get("/messages/chat/1007")
                .then()
                .statusCode(200)
                //https://github.com/rest-assured/rest-assured/wiki/usage
                .body(
                        "findAll { it.senderId != 1006 && it.recipientId != 1006 }.size()",
                        is(0)
                );
    }

    //GET messages/chats --> Chatübersicht liefert beide Chatpartner von Tom   Positive
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = {
            @Claim(key = "email", value = "test1@test.de"),
            @Claim(key = "preferred_username", value = "test1@test.de")
    })
    void chatsReturnsBothChatPartners() {
        given()
                .when()
                .get("/messages/chats")
                .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("userId", hasItems(1007, 1008));
    }

    //POST messages --> Tom sendet erfolgreich eine Nachricht an Lisa   Positive
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = {
            @Claim(key = "email", value = "test1@test.de"),
            @Claim(key = "preferred_username", value = "test1@test.de")
    })
    void sendCreatesMessage() {
        given()
                .contentType("application/json")
                .body("""
                    {
                        "recipientId": 1007,
                        "text": "Hallo Lisa, das ist eine Testnachricht.",
                        "offerId": null
                    }
                    """)
                .when()
                .post("/messages")
                .then()
                .statusCode(201)
                .body("recipientId", is(1007))
                .body("text", is("Hallo Lisa, das ist eine Testnachricht."))
                .body("id", notNullValue());
    }

    //POST messages --> Nicht angemeldeter Nutzer darf keine Nachricht senden   Negativ
    @Test
    void sendRequiresAuthentication() {
        given()
                .contentType("application/json")
                .body("""
                    {
                        "recipientId": 1007,
                        "text": "Nicht erlaubte Nachricht",
                        "offerId": null
                    }
                    """)
                .when()
                .post("/messages")
                .then()
                .statusCode(401);
    }

    //POST messages --> Nachricht ohne Text wird abgelehnt   Negativ
    @Test
    @TestSecurity(user = "test1@test.de")
    @OidcSecurity(claims = {
            @Claim(key = "email", value = "test1@test.de"),
            @Claim(key = "preferred_username", value = "test1@test.de")
    })
    void sendRejectsEmptyText() {
        given()
                .contentType("application/json")
                .body("""
                    {
                        "recipientId": 1007,
                        "text": "",
                        "offerId": null
                    }
                    """)
                .when()
                .post("/messages")
                .then()
                .statusCode(400);
    }
}
