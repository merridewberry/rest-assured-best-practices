package booker.endpoints.booking.id;


import booker.utils.Endpoints;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static booker.utils.Specs.requestSpec;
import static booker.utils.Specs.responseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class Id {

    static final String bookingId = Endpoints.bookingId;

    public static Response getBooking(int id) {
        return given()
                .spec(requestSpec)
                .get(bookingId, id);
    }

    public static Response updateBooking(int id, booker.pojo.Booking body) {
        return given()
                .spec(requestSpec)
                .body(body)
                .put(bookingId, id);
    }

    public static ValidatableResponse getBooking_valid(Response response) {
        return response
                .then()
                .spec(responseSpec(200))
                .body(matchesJsonSchemaInClasspath("schemas/Booking.json"));
    }

}
