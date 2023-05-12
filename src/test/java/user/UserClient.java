package user;

import data.Credentials;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.Validatable;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import pogo.AuthResponse;
import pogo.User;
import utils.Specification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

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
    @Step("Send PATCH request to /api/auth/user with new name")
    public ValidatableResponse sendPatchRequestNewUserName(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .body(Credentials.userWithNewName)
                .when()
                .patch("auth/user")
                .then()
                .statusCode(HttpStatus.SC_OK)
                ;}
    @Step("Send PATCH request to /api/auth/user with new email")
    public ValidatableResponse sendPatchRequestNewUserEmail(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .body(Credentials.userWithNewEmail)
                .when()
                .patch("auth/user")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
    @Step("Send PATCH request to /api/auth/user with new password without authorization")
    public ValidatableResponse sendPatchRequestWithoutAuthNewUserPassword() {
        return given()
                .body(Credentials.userWithNewPassword)
                .when()
                .patch("auth/user")
                .then();}
    @Step("Send PATCH request to /api/auth/user with new name without authorization")
    public ValidatableResponse sendPatchRequestWithoutAuthNewUserName() {
        return given()
                .body(Credentials.userWithNewName)
                .when()
                .patch("auth/user")
                .then();
    }
    @Step("Send PATCH request to /api/auth/user with new email without authorization")
    public ValidatableResponse sendPatchRequestWithoutAuthNewUserEmail() {
        return given()
                .body(Credentials.userWithNewEmail)
                .when()
                .patch("auth/user")
                .then();}
    @Step ("Delete user")
    public void deleteUser(String token){
        given()
                .header("Authorization",token)
                .when()
                .delete ("auth/user")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);}
    }