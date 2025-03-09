package com.myhobbies.online.entertainmentservice.cucumber;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import java.util.Map;

import static io.restassured.RestAssured.given;

@UtilityClass
public class RestAssuredUtils {

    public static RequestSpecification buildRequestSpecification(String baseUrl, Map<String, String> headers, Map<String, String> queryParams, String requestBody) {

        RequestSpecBuilder reqSpecBuilder = new RequestSpecBuilder().setRelaxedHTTPSValidation()
                .setBaseUri(baseUrl);
        if (headers != null && !headers.isEmpty()) {
            reqSpecBuilder.addHeaders(headers);
        }
        if (queryParams != null && !queryParams.isEmpty()) {
            reqSpecBuilder.addQueryParams(queryParams);
        }
        if (requestBody != null) {
            reqSpecBuilder.setBody(requestBody);
        }
        return reqSpecBuilder.build();
    }

    public static Response sendGetRequest(RequestSpecification requestSpecification, String endpoint) {
        return given().spec(requestSpecification)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response sendPostRequest(RequestSpecification requestSpecification, String endpoint) {
        return given().spec(requestSpecification)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response sendPutRequest(RequestSpecification requestSpecification, String endpoint) {
        return given().spec(requestSpecification)
                .contentType(ContentType.JSON)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    public static Response sendDeleteRequest(RequestSpecification requestSpecification, String endpoint) {
        return given().spec(requestSpecification)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    public static String getValueFromResponse(Response response, String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }
}