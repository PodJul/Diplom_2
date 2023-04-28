import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import pogo.AuthResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;


public class ChangeUserDataTest {
    UserClient userClient=new UserClient();
    AuthResponse logInResponse;
    String token;


    @Before
    public void setUp() {
        Specification.installSpec(Specification.requestSpec("https://stellarburgers.nomoreparties.site", "api"), Specification.responseSpec());
        Response response = userClient.createUser(Credentials.user);
        token = response.then().extract().path("accessToken");

    }
    @Test
    @DisplayName("Change user name with authorization")
    public void ChangeUserNameWithAuthorization() {
        logInResponse =userClient.userLoginAndCheckStatusCode(Credentials.userWithoutName);
        Response response = sendPatchRequestNewUserName();
        checkResponseAfterChangeName(response);
        deleteUserWithNewName();
            }
    @Step("Send PATCH request to /api/auth/user with new name")
    public Response sendPatchRequestNewUserName() {
              return given()
                .header("Authorization",logInResponse.getAccessToken())
                .body(Credentials.userWithNewName)
                .when()
                .patch("auth/user");

                 }
    @Step("Check body and status code after change name")
    public void checkResponseAfterChangeName (Response response) {
                 response.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body("success", is(true))
                .body("user.email", equalTo(Credentials.fakeEmail))
                .body ("user.name", equalTo(Credentials.newFakeName));

            }
    @Step ("Delete user with new name")
    public void deleteUserWithNewName(){
        given()
                .header("Authorization",logInResponse.getAccessToken())
                .body(Credentials.userWithNewName)
                .when()
                .delete ("auth/user")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);}
    @Test
    @DisplayName("Change user email with authorization")
    public void ChangeUserEmailWithAuthorization() {
        logInResponse =userClient.userLoginAndCheckStatusCode(Credentials.userWithoutName);
        Response response = sendPatchRequestNewUserEmail();
        checkResponseAfterChangeEmail (response);
        deleteUserWithNewEmail();
    }
    @Step("Send PATCH request to /api/auth/user with new email")
    public Response sendPatchRequestNewUserEmail() {
        return given()
                .header("Authorization",logInResponse.getAccessToken())
                .body(Credentials.userWithNewEmail)
                .when()
                .patch("auth/user");
    }
    @Step("Check body and status code after change email")
    public void checkResponseAfterChangeEmail (Response response) {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body("success", is(true))
                .body("user.email", equalTo(Credentials.newFakeEmail))
                .body ("user.name", equalTo(Credentials.fakeName));
    }
    @Step ("Delete user with new email")
    public void deleteUserWithNewEmail(){
        given()
                .header("Authorization",logInResponse.getAccessToken())
                .body(Credentials.userWithNewEmail)
                .when()
                .delete ("auth/user")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);}
    @Test
    @DisplayName("Change user password with authorization")
    public void ChangeUserPasswordWithAuthorization() {
        logInResponse =userClient.userLoginAndCheckStatusCode(Credentials.userWithoutName);
        Response response = sendPatchRequestNewUserPassword();
        checkResponseAfterChangePassword (response);
        deleteUserWithNewPassword();
    }
    @Step("Send PATCH request to /api/auth/user with new password")
    public Response sendPatchRequestNewUserPassword() {
        return given()
                .header("Authorization",logInResponse.getAccessToken())
                .body(Credentials.userWithNewPassword)
                .when()
                .patch("auth/user");
    }
    @Step("Check body and status code after change password")
    public void checkResponseAfterChangePassword (Response response) {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body("success", is(true))
                .body("user.email", equalTo(Credentials.fakeEmail))
                .body ("user.name", equalTo(Credentials.fakeName));
    }
    @Step ("Delete user with new password")
    public void deleteUserWithNewPassword(){
        given()
                .header("Authorization",logInResponse.getAccessToken())
                .body(Credentials.userWithNewPassword)
                .when()
                .delete ("auth/user")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);}
    @Test
    @DisplayName("Change user name without authorization")
    public void ChangeUserNameWithoutAuthorization() {
        Response response = sendPatchRequestWithoutAuthNewUserName();
        checkResponseAfterChangeNameWithoutAuth(response);
        tearDown();
        }
    @Step("Send PATCH request to /api/auth/user with new name without authorization")
    public Response sendPatchRequestWithoutAuthNewUserName() {
        return given()
                .body(Credentials.userWithNewName)
                .when()
                .patch("auth/user");
    }
    @Step("Check body after change name")
    public void checkResponseAfterChangeNameWithoutAuth (Response response) {
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat().body("success", is(false))
                .body("message", equalTo("You should be authorised"));}
    @Step ("Delete user")
    public void tearDown(){
        // авторизация пользователя
        userClient.userLogin(Credentials.userWithoutName);
        // удаление пользователя
        given()
                .header("Authorization",token)
                .body(Credentials.user)
                .when()
                .delete ("auth/user")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }
    @Test
    @DisplayName("Change user email without authorization")
    public void ChangeUserEmailWithoutAuthorization() {
        Response response = sendPatchRequestWithoutAuthNewUserEmail();
        checkResponseAfterChangeEmailWithoutAuth(response);
        tearDown();
        }
    @Step("Send PATCH request to /api/auth/user with new email without authorization")
    public Response sendPatchRequestWithoutAuthNewUserEmail() {
        return given()
                .body(Credentials.userWithNewEmail)
                .when()
                .patch("auth/user");
    }
    @Step("Check body after change email")
    public void checkResponseAfterChangeEmailWithoutAuth (Response response) {
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat().body("success", is(false))
                .body("message", equalTo("You should be authorised"));}
    @Test
    @DisplayName("Change user password without authorization")
    public void ChangeUserPasswordWithoutAuthorization() {
        Response response = sendPatchRequestWithoutAuthNewUserPassword();
        checkResponseAfterChangePasswordWithoutAuth(response);
        tearDown();
    }
    @Step("Send PATCH request to /api/auth/user with new password without authorization")
    public Response sendPatchRequestWithoutAuthNewUserPassword() {
        return given()
                .body(Credentials.userWithNewPassword)
                .when()
                .patch("auth/user");
    }
    @Step("Check body after change password")
    public void checkResponseAfterChangePasswordWithoutAuth (Response response) {
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat().body("success", is(false))
                .body("message", equalTo("You should be authorised"));}

}
