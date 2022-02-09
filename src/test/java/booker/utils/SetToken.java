package booker.utils;

import booker.pojo.Auth;

import static booker.utils.PropertiesManager.setProperty;
import static booker.utils.Specs.requestSpec;
import static io.restassured.RestAssured.given;

public class SetToken {

    static final String auth = Endpoints.auth;

    public static void setToken() {
        Auth authDefault = new Auth();

        String token = given()
                .spec(requestSpec)
                .body(authDefault)
                .post(auth)
                .then()
                .extract().path("token");

        setProperty("token", token);
    }
}
