package net.bddtrader.acceptancetests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.bddtrader.clients.Client;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class WhenUpdatingAndDeletingAClient {

    @Before
    public void setupBaseUrl(){
        RestAssured.baseURI = "http://localhost:9000/api";
    }
    @Test
    public void should_be_able_to_delete_a_client(){
        //Given a client exists
        String id = aClientExists(Client.withFirstName("Pam").andLastName("Beasley").andEmail("pan@beasley.com"));

        //When I delete the client

        //.pathParam("id", id).delete("/client/{id}")
        RestAssured.given().delete("/client/{id}", id);

        //Then the client should no longer exist

        //.pathParam("id", id).get("/client/{id}")
        RestAssured.given()
                .get("/client/{id}", id)
                .then().statusCode(404);
    }

    @Test
    public void should_be_able_to_update_a_client(){
        Client pam = Client.withFirstName("Pam").andLastName("Beasley").andEmail("pam@beasley.com");
        //Given a client exists
        String id = aClientExists(pam);

        //When I update the email address of a client
        Map<String, Object> updates = new HashMap<>();
        updates.put("email","pam@gmail.com");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .and().body(updates)
                .when().put("/client/{id}",id)
                .then().statusCode(200);

        //Then the client email address is updated correctly
        RestAssured.when().get("/client/{id}",id)
                .then().body("email",equalTo("pam@gmail.com"));
    }

    //Method for creating the new client and retrieving the id
    private static String aClientExists(Client existingClient) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(existingClient)
                .when().post("/client")
                .jsonPath().getString("id");
    }
}
