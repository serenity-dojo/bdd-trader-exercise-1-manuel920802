package net.bddtrader.acceptancetests;


import io.restassured.RestAssured;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;

@RunWith(SerenityRunner.class)
public class JSONPathAssertionExercisesWithSerenityBDD {

    @Before
    public void prepare_rest_api(){
        RestAssured.baseURI = "https://bddtrader.herokuapp.com/api/";
    }

    //GET method tests
    @Test
    public void find_a_simple_field_value(){
        SerenityRest.given()
                .pathParam("symbol", "aapl")
                .when().get("stock/{symbol}/company");

        Ensure.that("The industry is correctly defined",
                response -> response.body("industry", equalTo("Telecommunications Equipment")))
                .andThat("The exchange should be NASDAQ",
                        response -> response.body("exchange", equalTo("NASDAQ")));
    }

    @Test
    public void check_that_a_field_value_contains_a_given_string(){
        RestAssured.given().pathParam("symbol", "aapl")
                .when().get("stock/{symbol}/company")
                .then().body("description", containsString("smartphones"));
    }

    @Test
    public void find_a_nested_field_value(){
        RestAssured.given().pathParam("symbol", "aapl")
                .when().get("stock/{symbol}/book")
                .then().body("quote.symbol", equalTo("AAPL"));
    }

    @Test
    public void find_a_list_of_values(){
        RestAssured.when().get("tops/last")
                .then().body("symbol", hasItems("PTN", "PINE", "TRS"));
    }

    @Test
    public void make_sure_at_least_one_item_matches_a_given_condition(){
        RestAssured.when().get("tops/last")
                .then().body("price", hasItems(greaterThan(100.0f)));
    }

    @Test
    public void find_a_field_of_an_element_in_a_list(){
        RestAssured.given().pathParam("symbol", "aapl")
                .when().get("stock/{symbol}/book")
                .then().body("trades[0].price", equalTo(319.59f));
    }

    @Test
    public void find_a_field_of_the_last_element_in_a_list(){
        RestAssured.given().pathParam("symbol", "aapl")
                .when().get("stock/{symbol}/book")
                .then().body("trades[-1].price", equalTo(319.54f));
    }

    @Test
    public void find_the_number_of_trades(){
        RestAssured.given().pathParam("symbol", "aapl")
                .when().get("stock/{symbol}/book")
                .then().body("trades.size()", equalTo(20));
    }

    @Test
    public void find_the_minimum_trade_price(){
        RestAssured.given().pathParam("symbol", "aapl")
                .when().get("stock/{symbol}/book")
                .then().body("trades.price.min()", equalTo(319.38f));
    }

    @Test
    public void find_the_volume_of_the_trade_with_the_minimum_trade_price(){
        RestAssured.given().pathParam("symbol", "aapl")
                .when().get("stock/{symbol}/book")
                .then().body("trades.min { it.price }.volume", equalTo(100.0f));
    }

    @Test
    public void find_the_number_of_trades_with_a_price_greater_than_some_value(){
        RestAssured.given().pathParam("symbol", "aapl")
                .when().get("stock/{symbol}/book")
                .then().body("trades.findAll { it.price > 319.50 }.size()", equalTo(13));
    }
}
