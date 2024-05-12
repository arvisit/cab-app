package by.arvisit.cabapp.driverservice.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.util.DriverITData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CarManufacturerControllerSteps {

    private Response response;

    @Given("User wants to get details about existing car manufacturers")
    public void prepareInfoForRetrievingCarManufacturers() {
    }

    @When("he performs request for car manufacturers with no request parameters")
    public void sendGetCarManufacturersWithNoRequestParamsRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(DriverITData.URL_CAR_MANUFACTURERS);
    }

    @Then("response should have 200 status, json content type, contain info about these car manufacturers from page 0:")
    public void checkGetCarManufacturersWithNoRequestParams(DataTable table) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<CarManufacturerResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<CarManufacturerResponseDto>>() {
                });

        List<CarManufacturerResponseDto> expectedCarManufacturers = extractCarManufacturers(table);

        assertThat(actual.currentPage())
                .isZero();
        assertThat(actual.lastPage())
                .isOne();
        assertThat(actual.size())
                .isEqualTo(DriverITData.DEFAULT_PAGEABLE_SIZE);
        assertThat(actual.sort())
                .isEqualTo(DriverITData.UNSORTED);

        assertThat(actual.values())
                .containsExactlyInAnyOrderElementsOf(expectedCarManufacturers);
    }

    @When("he performs request for car manufacturers with request parameters: {string}={int}")
    public void sendGetCarManufacturersWithPageRequestParamRequest(String pageParam, int pageValue) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(pageParam, pageValue)
                .when().get(DriverITData.URL_CAR_MANUFACTURERS);
    }

    @Then("response should have 200 status, json content type, contain info about these car manufacturers from page 1:")
    public void checkGetCarManufacturersWithPageRequestParam(DataTable table) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<CarManufacturerResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<CarManufacturerResponseDto>>() {
                });

        List<CarManufacturerResponseDto> expectedCarManufacturers = extractCarManufacturers(table);

        assertThat(actual.currentPage())
                .isOne();
        assertThat(actual.lastPage())
                .isOne();
        assertThat(actual.size())
                .isEqualTo(DriverITData.DEFAULT_PAGEABLE_SIZE);
        assertThat(actual.sort())
                .isEqualTo(DriverITData.UNSORTED);

        assertThat(actual.values())
                .containsExactlyInAnyOrderElementsOf(expectedCarManufacturers);
    }

    private List<CarManufacturerResponseDto> extractCarManufacturers(DataTable table) {
        List<CarManufacturerResponseDto> carManufacturers = new ArrayList<>();
        List<List<String>> rows = table.asLists(String.class);
        for (List<String> columns : rows) {
            int id = Integer.parseInt(columns.get(0));
            String name = columns.get(1);
            carManufacturers.add(CarManufacturerResponseDto.builder()
                    .withId(id)
                    .withName(name)
                    .build());
        }
        return carManufacturers;
    }
}
