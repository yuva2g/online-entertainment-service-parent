package com.myhobbies.online.entertainmentservice.cucumber.stepdefinition;

import com.myhobbies.online.entertainmentservice.cucumber.RestAssuredUtils;
import com.myhobbies.online.entertainmentservice.models.Entertainment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.AssertJUnit;

import java.util.List;

public class EntertainmentServiceStepDefinitions {

    private final String baseUrl = "http://localhost:8095";
    private Response response;

    @Given("the entertainment service is running")
    public void the_entertainment_service_is_running() {
        // request actuator endpoint to see if service is running
        Response response = RestAssuredUtils.sendGetRequest(RestAssuredUtils.buildRequestSpecification(baseUrl, null, null, null),
                "/actuator/health");
        response.then().statusCode(200);
        response.then().body("status", org.hamcrest.Matchers.equalTo("UP"));

    }

    @When("I request entertainment options with search text {string}")
    public void i_request_entertainment_options_with_search_text(String searchText) {
        response = RestAssuredUtils.sendGetRequest(RestAssuredUtils.buildRequestSpecification(baseUrl, null, null, null),
                "/online-entertainments?searchText=" + searchText);
    }

    @Then("I should receive a sorted list of entertainment options with status code {int}")
    public void i_should_receive_a_sorted_list_of_entertainment_options_with_status_code(int statusCode) {
        response.then().statusCode(statusCode);
        List<Entertainment> result = response.jsonPath().getList("", Entertainment.class);

        AssertJUnit.assertEquals(10, result.size()); // 5 from albums and 5 from books
        checkIfResultIsSorted(result);
    }

    private void checkIfResultIsSorted(List<Entertainment> result) {
        // check if result is sorted
        for (int i = 0; i < result.size() - 1; i++) {
            AssertJUnit.assertTrue(String.format("%s is before %s", result.get(i).getTitle(), result.get(i + 1).getTitle()),
                    result.get(i).getTitle().compareTo(result.get(i + 1).getTitle()) < 0);
        }
    }

    @Then("I should receive an error response with status code {int}")
    public void i_should_receive_an_error_response_with_status_code(int statusCode) {
        response.then().statusCode(statusCode);
    }
}
