package net.bddtrader.acceptancetests;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class WhenGettingCompanyDetails {

    @Before
    public void prepare_rest_config(){
        RestAssured.baseURI = "https://bddtrader.herokuapp.com/api";
    }

    //Note:When authentication is needed from the API:

    // Basic authentication: Passes credentials over the network (not very secure, use digest instead)
    // Provide Username and Password: .auth().basic("user", "password")
    // Send in the header of each request (this avoids an extra query): .auth().preemptive().basic("user", "password")

    // Digest authentication: More secure than basic auth because it does not pass the real password over the network
    // Provide Username and Password: auth().digest("user", "password")

    // Form authentication: Using an HTML form to pass credentials
    // Provide Username and Password: auth().form("user", "password" new FormAuthConfig("/j_spring_security_check", "j_username", "j_password"))

    @Test
    public void should_return_name_and_sector() {
        // we can use the param in the url : "/stock/aapl/company"
        // or send it has a param:
        RestAssured.get("/stock/{symbol}/company", "aapl")
                .then()
                .body("companyName", equalTo("Apple, Inc."))
                .body("sector", equalTo("Electronic Technology"));
    }

    @Test
    public void should_return_name_and_sector_using_path_param() {

        RestAssured.given()
                .pathParam("symbol", "aapl")
                .when()
                .get("/stock/{symbol}/company")
                .then()
                .body("companyName", equalTo("Apple, Inc."))
                .body("sector", equalTo("Electronic Technology"))
                .body("industry", equalTo("Telecommunications Equipment"));
    }

    @Test
    public void should_return_news_for_a_requested_company_using_query_param(){
        RestAssured.given()
                .queryParam("symbols", "fb")
                .when()
                .get("/news")
                .then()
                .body("related", everyItem(containsString("FB")));
    }
}
