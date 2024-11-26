package rest.config;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class ApiConfig {

    private static final String BASE_URI = "https://jsonplaceholder.typicode.com";

    public static RequestSpecification getRequestSpec() {
        return RestAssured
                .given()
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json");
    }
}
