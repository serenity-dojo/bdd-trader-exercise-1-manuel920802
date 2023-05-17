package net.bddtrader.steps;

import io.restassured.http.ContentType;
import net.bddtrader.clients.Client;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class PlatformAdminSteps {

    private String actor;
    @Step("#actor registers a new client {0}")
    public String registerANewClient(Client newClient) {
        SerenityRest.given()
                .contentType(ContentType.JSON)
                .body(newClient)
                .when().post("/client")
                .then().statusCode(200)
                .and().body("id",not(equalTo(0)));

        return SerenityRest.lastResponse().jsonPath().getString("id");
    }

    @Step("#actor searches for a client with id of {0}")
    public void searchesForAClientById(String clientId) {
        SerenityRest.when().get("/client/{id}", clientId)
                .then().statusCode(200);
    }

    @Step("#actor finds a client matching {0}")
    public void findAClientMatching(Client expectedClient) {
        Ensure.that("First name is " + expectedClient.getFirstName(),
                response -> response.body("firstName", equalTo(expectedClient.getFirstName())));

        Ensure.that("Last name is " + expectedClient.getLastName(),
                response -> response.body("lastName", equalTo(expectedClient.getLastName())));

        Ensure.that("Email is " + expectedClient.getEmail(),
                response -> response.body("email", equalTo(expectedClient.getEmail())));
    }
}
