package utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specification {
    public static RequestSpecification requestSpec(String baseUrl, String basePath){
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath(basePath)
                .setContentType("application/json")
                .log(LogDetail.ALL)
                .build();
    }
    public static ResponseSpecification responseSpec (){
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }
    public static void installSpec (RequestSpecification request, ResponseSpecification response){
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;

    }
}
