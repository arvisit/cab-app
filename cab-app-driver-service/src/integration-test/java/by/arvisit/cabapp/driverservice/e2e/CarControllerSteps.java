package by.arvisit.cabapp.driverservice.e2e;

import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_CARS;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_CARS_ID_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJohnDoeCar;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CarControllerSteps {

    @Given("User wants to get details about existing cars")
    public void prepareInfoForRetrievingCars() {
    }

    private Response response;
    private String idToGetCarBy;

    @When("he performs request for cars with no request parameters")
    public void sendGetCarsWithNoRequestParamsRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_CARS);
    }

    @Then("response should have 200 status, json content type, contain info about {int} cars")
    public void checkGetCarsWithNoRequestParams(int carsCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<CarResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<CarResponseDto>>() {
                });

        assertThat(actual.values())
                .hasSize(carsCount);
    }

    @Given("User wants to get details about an existing car with id {string}")
    public void prepareInfoForRetrievingCarById(String id) {
        idToGetCarBy = id;
    }

    @When("he performs search car by id via request")
    public void sendGetCarByIdRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_CARS_ID_TEMPLATE, idToGetCarBy);
    }

    @Then("response should have 200 status, json content type, contain car with requested id")
    public void checkGetCarByIdResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        CarResponseDto actual = response.as(CarResponseDto.class);
        CarResponseDto expected = getJohnDoeCar().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
