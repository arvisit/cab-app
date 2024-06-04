package by.arvisit.cabapp.driverservice.component;

import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.UNSORTED;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_CARS;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_CARS_ID_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJaneDoeCar;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJannyDoeCar;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJohnDoeCar;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJohnnyDoeCar;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getListContainerForResponse;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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

        ListContainerResponseDto<CarResponseDto> expected = getListContainerForResponse(CarResponseDto.class)
                .withValues(List.of(getJaneDoeCar().build(), getJannyDoeCar().build(), getJohnnyDoeCar().build(),
                        getJohnDoeCar().build()))
                .withLastPage(0)
                .build();

        assertThat(actual.currentPage())
                .isZero();
        assertThat(actual.lastPage())
                .isZero();
        assertThat(actual.size())
                .isEqualTo(DEFAULT_PAGEABLE_SIZE);
        assertThat(actual.sort())
                .isEqualTo(UNSORTED);

        assertThat(actual.values())
                .containsExactlyInAnyOrderElementsOf(expected.values());
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
