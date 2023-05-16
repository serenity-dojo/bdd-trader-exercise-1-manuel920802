package net.bddtrader.acceptancetests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.bddtrader.clients.Client;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class WhenCreatingANewClient {

    @Before
    public void setupBaseUrl(){
        RestAssured.baseURI = "http://localhost:9000/api";
    }

    //POST method tests
    @Test
    public void each_new_client_should_get_a_unique_id() {

        //Can create new client using existing and object class with builder pattern (or a simple constructor):
        /*Client aNewClient = Client.withFirstName("Michael")
                .andLastName("Scott").andEmail("michael@scott.com");*/

        //Can create new client using maps function:
       /* Map<String, Object> clientData = new HashMap<>();
        clientData.put("email", "michael@scott.com");
        clientData.put("firstName","Michael");
        clientData.put("lastName", "Scott");*/

       // Can create new client passing json string:
         String newClient = "{\n" +
                " \"email\": \"michael@scott.com\", \n" +
                " \"firstName\": \"Michael\",\n" +
                " \"lastName\": \"Scott\"\n" +
                "}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newClient)
                .when().post("/client")
                .then().statusCode(200)
                .and().body("id",not(equalTo(0)))
                .and().body("email", equalTo("michael@scott.com"))
                .and().body("firstName", equalTo("Michael"))
                .and().body("lastName", equalTo("Scott"));
    }

    @Test
    public void should_create_new_client_with_client_class(){
        Client aNewClient = Client.withFirstName("Jim")
                .andLastName("Halpert").andEmail("jim@halpert.com");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(aNewClient)
                .when().post("/client")
                .then().statusCode(200)
                .and().body("id",not(equalTo(0)))
                .and().body("email",equalTo("jim@halpert.com"))
                .and().body("firstName",equalTo("Jim"))
                .and().body("lastName",equalTo("Halpert"));
    }

    @Test
    public void should_create_a_new_client_with_map(){
        Map<String, Object> clientData = new HashMap<>();
        clientData.put("email","kevin@malone.com");
        clientData.put("firstName","Kevin");
        clientData.put("lastName","Malone");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(clientData)
                .when().post("/client")
                .then().statusCode(200)
                .and().body("id",not(equalTo(0)))
                .and().body("email",equalTo("kevin@malone.com"))
                .and().body("firstName",equalTo("Kevin"))
                .and().body("lastName",equalTo("Malone"));

    }
}
