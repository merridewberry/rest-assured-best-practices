package booker.utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static booker.utils.PropertiesManager.getProperty;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class Specs {
    static final String BASE_URI = getProperty("baseUri");
    static final Long responseTime = Long.valueOf(getProperty("responseTime"));

    public static RequestSpecification requestSpec = new RequestSpecBuilder()
            .addFilter(new AllureRestAssured())
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .setAccept("application/json")
            .addHeader("Cookie", "token=" + getProperty("token"))
            .build();

    public static ResponseSpecification responseSpec(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectResponseTime(lessThanOrEqualTo(responseTime))
                .build();
    }

}
