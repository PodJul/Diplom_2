import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import pogo.AuthResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class CreateUserTest {
    AuthResponse regResponse;
    UserClient userClient=new UserClient();
    @Before
    public void setUp() {
        Specification.installSpec(Specification.requestSpec("https://stellarburgers.nomoreparties.site", "api"), Specification.responseSpec());
 }
    @Test
    @DisplayName("Create new user and check response")
    public void createNewUserAndCheckResponse() {
        regResponse=userClient.createUserAndCheckStatusCode(Credentials.user);
        checkBodyOfResponse(regResponse);
        tearDown();
    }
    @Step("Check body of response")
    public void checkBodyOfResponse (AuthResponse response) {
        assertTrue(regResponse.isSuccess());
        assertEquals(Credentials.fakeEmail,regResponse.getUser().getEmail());
        assertEquals(Credentials.fakeName,regResponse.getUser().getName());
        assertFalse(regResponse.getAccessToken().isBlank());
        assertFalse(regResponse.getRefreshToken().isBlank());
            }
    @Step ("Delete user")
    public void tearDown(){
        // авторизация пользователя
        userClient.userLogin(Credentials.userWithoutName);
        // удаление пользователя
        given()
                .header("Authorization",regResponse.getAccessToken())
                .body(Credentials.user)
                .when()
                .delete ("auth/user")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }
    @Test
    @DisplayName("Create double user and check response")
    public void createDoubleUserAndCheckResponse() {

        regResponse = userClient.createUserAndCheckStatusCode(Credentials.user);
        checkBodyOfResponse(regResponse);
        Response newResponse = sendDoublePostRequestUser();
        checkStatusCodeOfBadRequest(newResponse);
        checkBodyOfDoubleRequest(newResponse);
        tearDown();
    }
    @Step("Send double POST request to /api/v1/courier")
    public Response sendDoublePostRequestUser() {
        return userClient.createUser(Credentials.user);}

    @Step("Check statusCode of double request")
    public void checkStatusCodeOfBadRequest(Response newResponse) {
        assertEquals(HttpStatus.SC_FORBIDDEN,newResponse.getStatusCode());
    }
    @Step("Check body of double request")
    public void checkBodyOfDoubleRequest(Response newResponse) {
        newResponse.then().body("success", is(false)).
                assertThat().body("message", equalTo("User already exists"));
    }
    @Test
    @DisplayName("Create user without email and check response")
    public void createUserWithoutEmailAndCheckResponse () {
        Response response = sendPostRequestUserWithoutEmail();
        checkStatusCodeOfBadRequest(response);
        checkMessageToRequestWithoutAnyField (response);

    }
    @Step("Send POST request to /api/auth/register without email")
    public Response sendPostRequestUserWithoutEmail () {
        return userClient.createUser(Credentials.userWithoutEmail);}

    @Step("Check message in response to the request without any field")
    public void checkMessageToRequestWithoutAnyField (Response response){
        response.then().body("success", is(false))
                .and().assertThat().body("message", equalTo("Email, password and name are required fields"));}
    @Test
    @DisplayName("Create user without password and check response")
    public void createUserWithoutPasswordAndCheckResponse () {
        Response response = sendPostRequestUserWithoutPassword();
        checkStatusCodeOfBadRequest(response);
        checkMessageToRequestWithoutAnyField (response);
    }
    @Step("Send POST request to /api/auth/register without password")
    public Response sendPostRequestUserWithoutPassword () {
        return userClient.createUser(Credentials.userWithoutPassword);}
    @Test
    @DisplayName("Create user without name and check response")
    public void createUserWithoutNameAndCheckResponse () {
        Response response = sendPostRequestUserWithoutName();
        checkStatusCodeOfBadRequest(response);
        checkMessageToRequestWithoutAnyField (response);
    }
    @Step("Send POST request to /api/auth/register without name")
    public Response sendPostRequestUserWithoutName () {
        return userClient.createUser(Credentials.userWithoutName);}

}