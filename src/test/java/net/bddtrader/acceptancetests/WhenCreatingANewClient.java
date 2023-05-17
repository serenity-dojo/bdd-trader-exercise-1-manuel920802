package net.bddtrader.acceptancetests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.bddtrader.clients.Client;
import net.bddtrader.steps.PlatformAdminSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(SerenityRunner.class)
public class WhenCreatingANewClient {

    @Before
    public void setupBaseUrl(){
        //Note: Use "mvn spring-boot:run" on terminal to run api locally on port 9000
        RestAssured.baseURI = "http://localhost:9000/api";
    }
    @Steps
    PlatformAdminSteps pat;

    //POST method tests
    @Test
    public void each_new_client_should_get_a_unique_id() {

        Client michaelScott = Client
                .withFirstName("Michael")
                .andLastName("Scott")
                .andEmail("michael@scott.com");

        String clientId = pat.registerANewClient(michaelScott);

        pat.searchesForAClientById(clientId);

        pat.findAClientMatching(michaelScott);
    }

    @Test
    public void should_create_new_client_with_client_class(){
        Client jimHarper = Client
                .withFirstName("Jim")
                .andLastName("Halpert")
                .andEmail("jim@halpert.com");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(jimHarper)
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
