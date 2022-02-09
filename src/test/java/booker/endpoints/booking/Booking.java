package booker.endpoints.booking;

import booker.utils.Endpoints;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static booker.utils.Specs.requestSpec;
import static booker.utils.Specs.responseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class Booking {

    static final String booking = Endpoints.booking;

    public static Response createBooking(booker.pojo.Booking body) {
        return given()
                .spec(requestSpec)
                .body(body)
                .post(booking);
    }

    public static ValidatableResponse createBooking_valid(Response response) {
        return response
                .then()
                .spec(responseSpec(200))
                .body(matchesJsonSchemaInClasspath("schemas/Post.json"));
    }

    public static Response getBookings() {
        return given()
                .spec(requestSpec)
                .get(booking);
    }

    public static ValidatableResponse getBookings_valid(Response response) {
        return response
                .then()
                .spec(responseSpec(200))
                .body(matchesJsonSchemaInClasspath("schemas/BookingIds.json"));
    }

}
