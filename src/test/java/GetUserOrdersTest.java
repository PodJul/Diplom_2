import data.Credentials;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pogo.AuthResponse;
import user.UserClient;
import utils.Specification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class GetUserOrdersTest {
    UserClient userClient=new UserClient();
    AuthResponse logInResponse;

    @Before
    public void setUp() {
        Specification.installSpec(Specification.requestSpec("https://stellarburgers.nomoreparties.site", "api"), Specification.responseSpec());
        logInResponse=userClient.createUserAndCheckStatusCode(Credentials.user);
    }
    @After
    @Step("Delete user")
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
    @DisplayName("Get user orders with authorization")
    public void getUserOrderWithAuthorization() {

        userClient.userLoginAndCheckStatusCode(Credentials.userWithoutName);
        Response response=sendGetRequestToGetListOfOrders();
        checkResponseWhenGetListOfOrders(response);
    }
    @Step("Send GET request to /api/orders to get list of orders")
    public Response sendGetRequestToGetListOfOrders() {
        return  given()
                .header("Authorization",logInResponse.getAccessToken())
                .when()
                .get("orders");}

    @Step("Check body and status code when get list of orders")
    public void checkResponseWhenGetListOfOrders (Response response) {
        response.then()
                .statusCode(HttpStatus.SC_OK).body("success",is(true))
                .and().body("orders",notNullValue()).body("total",notNullValue()).body("totalToday",notNullValue());
    }
    @Test
    @DisplayName("Get user orders without authorization")
    public void getUserOrderWithoutAuthorization() {

        Response response=sendGetRequestToGetListOfOrdersWithoutAuth();
        checkResponseWhenGetListOfOrdersWithoutAuthorization(response);
    }
    @Step("Send GET request to /api/orders to get list of orders without authorization")
    public Response sendGetRequestToGetListOfOrdersWithoutAuth() {
        return  given()
                .when()
                .get("orders");}
    @Step("Check body and status code when get list of orders without authorization")
    public void checkResponseWhenGetListOfOrdersWithoutAuthorization (Response response) {
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).body("success",is(false))
                .and().body("message",equalTo("You should be authorised"));
    }

}
