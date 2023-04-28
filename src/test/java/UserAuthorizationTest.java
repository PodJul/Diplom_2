import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import pogo.AuthResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


public class UserAuthorizationTest {
    UserClient userClient=new UserClient();
    AuthResponse logInResponse;
    Response response;
    @Before
    public void setUp() {

        Specification.installSpec(Specification.requestSpec("https://stellarburgers.nomoreparties.site", "api"), Specification.responseSpec());
        userClient.createUser(Credentials.user);}
    @Test
    @DisplayName("User authorization")
    public void checkUserCanLogIn() {
        logInResponse = userClient.userLoginAndCheckStatusCode(Credentials.userWithoutName);
        checkBodyOfLogInResponse(logInResponse);
        tearDown();
        }
    @Step("Check body of log in response")
    public void checkBodyOfLogInResponse (AuthResponse response) {
        assertTrue(logInResponse.isSuccess());
        assertFalse(logInResponse.getAccessToken().isBlank());
        assertFalse(logInResponse.getRefreshToken().isBlank());
        assertEquals (Credentials.fakeEmail, logInResponse.getUser().getEmail());
        assertEquals(Credentials.fakeName,logInResponse.getUser().getName());
    }
    @Step ("Delete user")
    public void tearDown(){
        given()
                .header("Authorization",logInResponse.getAccessToken())
                .body(Credentials.user)
                .when()
                .delete ("auth/user")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }

    @Test
    @DisplayName("LogIn with wrong login and password and check response")
    public void LogInWithWrongFieldsAndCheckResponse() {
        response = sendPostRequestWithWrongFieldsAndCheckStatusCode();
        checkStatusCodeToRequestWithWrongFields (response);
        checkMessageToRequestWithWrongFields(response);

    }
    @Step
    public Response sendPostRequestWithWrongFieldsAndCheckStatusCode() {
        return userClient.userLogin(Credentials.newUser);}

    @Step("Check statusCode")
    public void checkStatusCodeToRequestWithWrongFields(Response response) {
        response.then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
    @Step("Check message in response to the request with wrong login and password")
    public void checkMessageToRequestWithWrongFields (Response response){
        response.then().body("success", is(false))
                .and().assertThat().body("message", equalTo("email or password are incorrect"));}
    }
