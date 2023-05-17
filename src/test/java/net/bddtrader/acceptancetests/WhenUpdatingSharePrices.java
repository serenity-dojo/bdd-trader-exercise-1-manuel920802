package net.bddtrader.acceptancetests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SerenityRunner.class)
public class WhenUpdatingSharePrices {

    @Before
    public void setupBaseUrl(){
        //Note: Use "mvn spring-boot:run" on terminal to run api locally on port 9000
        RestAssured.baseURI = "http://localhost:9000/api";
    }
    @Test
    public void should_be_able_to_update_a_share_price(){
        given()
                .pathParam("stock","aapl")
                .contentType(ContentType.JSON)
                .body("499.99")
                .and().post("/stock/{stock}/price");

        when().get("/stock/{stock}/price", "appl")
                .then().statusCode(200);

        String updatedPrice = lastResponse().body().asString();

        assertThat(updatedPrice).isEqualTo("499.99");
    }
}
