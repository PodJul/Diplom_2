import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import pogo.AuthResponse;
import pogo.User;

import static io.restassured.RestAssured.given;

public class UserClient {
    @Before
    public void setUp() {

        Specification.installSpec(Specification.requestSpec("https://stellarburgers.nomoreparties.site", "api"), Specification.responseSpec());
    }

    @Step("Send POST request to /api/auth/register and check statusCode")
    public AuthResponse createUserAndCheckStatusCode(User user) {

        return given()
                .body(user)
                .when()
                .post("auth/register")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response().as(AuthResponse.class);
    }
    @Step("Send POST request to /api/auth/register")
    public Response createUser(User user) {

        return given()
                .body(user)
                .when()
                .post("auth/register");

    }
    @Step("Send POST request to /api/auth/login and check statusCode")
    public AuthResponse userLoginAndCheckStatusCode(User user) {
        return given()
                .body(user)
                .when()
                .post("auth/login")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response().as(AuthResponse.class);
    }
    @Step("Send POST request to /api/auth/login")
    public Response userLogin(User user) {
        return given()
                .body(user)
                .when()
                .post("auth/login");

    }
    }