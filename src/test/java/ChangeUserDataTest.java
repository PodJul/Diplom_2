import data.Credentials;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserClient;
import utils.Specification;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class ChangeUserDataTest {
    UserClient userClient=new UserClient();
    String token;

    @Before
    public void setUp() {
        Specification.installSpec(Specification.requestSpec("https://stellarburgers.nomoreparties.site", "api"), Specification.responseSpec());
        Response response = userClient.createUser(Credentials.user);
        token = response.then().extract().path("accessToken");}
    @After
    public void tearDown(){
        userClient.deleteUser(token);}
    @Test
    @DisplayName("Change user name with authorization")
    public void ChangeUserNameWithAuthorization() {
        userClient.userLoginAndCheckStatusCode(Credentials.userWithoutName);
        ValidatableResponse response =userClient.sendPatchRequestNewUserName(token);
        response.assertThat().body("success", is( true))
                .body ("user.name", equalTo(Credentials.newFakeName))
                .body ("user.email", equalTo(Credentials.fakeEmail));
        }
    @Test
    @DisplayName("Change user email with authorization")
    public void ChangeUserEmailWithAuthorization() {
        userClient.userLoginAndCheckStatusCode(Credentials.userWithoutName);
        ValidatableResponse response = userClient.sendPatchRequestNewUserEmail(token);
        response.assertThat().body("success", is( true))
                .body ("user.name", equalTo(Credentials.fakeName))
                .body ("user.email", equalTo(Credentials.newFakeEmail));}

    @Test
    @DisplayName("Change user name without authorization")
    public void ChangeUserNameWithoutAuthorization() {
        ValidatableResponse response = userClient.sendPatchRequestWithoutAuthNewUserName();
        response.assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and().body("success", is(false))
                .body("message", equalTo("You should be authorised"));
        }
    @Test
    @DisplayName("Change user email without authorization")
    public void ChangeUserEmailWithoutAuthorization() {
        ValidatableResponse response = userClient.sendPatchRequestWithoutAuthNewUserEmail();
        response.assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and().body("success", is(false))
                .body("message", equalTo("You should be authorised"));}

    @Test
    @DisplayName("Change user password without authorization")
    public void ChangeUserPasswordWithoutAuthorization() {
        ValidatableResponse response = userClient.sendPatchRequestWithoutAuthNewUserPassword();;
        response.assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and().body("success", is(false))
                .body("message", equalTo("You should be authorised"));}
    }

