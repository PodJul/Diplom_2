import data.Credentials;
import ingredientList.IngredientsListGenerator;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pogo.*;
import user.UserClient;
import utils.Specification;

import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;


public class CreateOrderTest {
    UserClient userClient=new UserClient();
    AuthResponse logInResponse;
    IngredientsResponse ingredients;
    Ingredients ingredientsList;
    AuthResponse createResponse;

    @Before
    public void setUp() {
        Specification.installSpec(Specification.requestSpec("https://stellarburgers.nomoreparties.site", "api"), Specification.responseSpec());
        createResponse = userClient.createUserAndCheckStatusCode(Credentials.user);}
    @After
    @Step("Delete user")
    public void tearDown(){

        given()
                .header("Authorization",createResponse.getAccessToken())
                .body(Credentials.user)
                .when()
                .delete ("auth/user")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);
}
    @Test
    @DisplayName("Create oder with authorization")
    public void createOrderWithAuthorization() {

        logInResponse = userClient.userLoginAndCheckStatusCode(Credentials.userWithoutName);
        ingredients=sendGetRequestToGetListOfIngredients();
        IngredientsListGenerator ingredientsListGenerator = new IngredientsListGenerator();
        ArrayList<String> randomIngredientsList=ingredientsListGenerator.createIngredientsList(ingredients);
        ingredientsList = new Ingredients().setIngredients(randomIngredientsList);
        Response response = sendPostRequestCreateOrder();
        checkResponseOfCreateOrder(response);
    }
    @Step("Send GET request to /api/ingredients to get list of ingredients")
    public IngredientsResponse sendGetRequestToGetListOfIngredients() {
        return  given()
                .when()
                .get("ingredients")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response().as(IngredientsResponse.class);
    }
    @Step("Send POST request to /api/orders to create order")
    public Response sendPostRequestCreateOrder() {

                return given()
                .header("Authorization",logInResponse.getAccessToken())
                .body(ingredientsList)
                .when()
                .post("orders");}

    @Step("Check body and status code after create order with authorization")
    public void checkResponseOfCreateOrder (Response response) {
        response.then()
                .statusCode(HttpStatus.SC_OK).body("success",is(true))
                .and().body("name",notNullValue()).body("order",notNullValue());
    }
    //Этот тест будет падать из-за бага:заказ создается без авторизации
    @Test
    @DisplayName("Create oder without authorization")
    public void createOrderWithoutAuthorization() {

        ingredients=sendGetRequestToGetListOfIngredients();
        IngredientsListGenerator ingredientsListGenerator = new IngredientsListGenerator();
        ArrayList<String> randomIngredientsList=ingredientsListGenerator.createIngredientsList(ingredients);
        ingredientsList = new Ingredients().setIngredients(randomIngredientsList);
        Response response = sendPostRequestCreateOrderWithoutAuth();
        checkResponseOfCreateOrderWithoutAuth(response);
        }
    @Step("Send POST request to /api/orders to create order")
    public Response sendPostRequestCreateOrderWithoutAuth() {
        return given()
                .body(ingredientsList)
                .when()
                .post("orders");}

    @Step("Check body and status code after create order without authorization")
    public void checkResponseOfCreateOrderWithoutAuth (Response response) {
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED).body("success",is(false))
                .and().body("message",equalTo("You should be authorised"));}
    @Test
    @DisplayName("Create oder without ingredients")
    public void createOrderWithoutIngredients() {
        logInResponse = userClient.userLoginAndCheckStatusCode(Credentials.userWithoutName);
        ingredients=sendGetRequestToGetListOfIngredients();
        ingredientsList = new Ingredients().setIngredients(new ArrayList<>(List.of(new String[]{})));
        Response response = sendPostRequestCreateOrder();
        checkResponseOfCreateOrderWithoutIngredients(response);
            }
    @Step("Check body and status code after create order without ingredients")
    public void checkResponseOfCreateOrderWithoutIngredients (Response response) {
        response.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST).body("success",is(false))
                .and().body("message",equalTo("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Create oder with wrong ingredients")
    public void createOrderWithWrongIngredients() {
        logInResponse = userClient.userLoginAndCheckStatusCode(Credentials.userWithoutName);
        ingredients=sendGetRequestToGetListOfIngredients();
        IngredientsListGenerator ingredientsListGenerator = new IngredientsListGenerator();
        ingredientsList = new Ingredients().setIngredients(new ArrayList<>(List.of(new String[]{"","",""})));
        Response response = sendPostRequestCreateOrder();
        checkResponseOfCreateOrderWithWrongIngredients(response);
    }
    @Step("Check body and status code after create order without ingredients")
    public void checkResponseOfCreateOrderWithWrongIngredients (Response response) {
        response.then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
