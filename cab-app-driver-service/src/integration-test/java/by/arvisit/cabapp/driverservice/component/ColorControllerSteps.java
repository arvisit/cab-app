package by.arvisit.cabapp.driverservice.component;

import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.UNSORTED;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_COLORS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ColorControllerSteps {

    private Response response;

    @Given("User wants to get details about existing colors")
    public void prepareInfoForRetrievingColors() {
    }

    @When("he performs request for colors with no request parameters")
    public void sendGetColorsWithNoRequestParamsRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_COLORS);
    }

    @Then("response should have 200 status, json content type, contain info about these colors from page 0:")
    public void checkGetColorsWithNoRequestParams(DataTable table) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<ColorResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<ColorResponseDto>>() {
                });

        List<ColorResponseDto> expectedColors = extractColors(table);

        assertThat(actual.currentPage())
                .isZero();
        assertThat(actual.lastPage())
                .isOne();
        assertThat(actual.size())
                .isEqualTo(DEFAULT_PAGEABLE_SIZE);
        assertThat(actual.sort())
                .isEqualTo(UNSORTED);

        assertThat(actual.values())
                .containsExactlyInAnyOrderElementsOf(expectedColors);
    }

    @When("he performs request for colors with request parameters: {string}={int}")
    public void sendGetColorsWithPageRequestParamRequest(String pageParam, int pageValue) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(pageParam, pageValue)
                .when().get(URL_COLORS);
    }

    @Then("response should have 200 status, json content type, contain info about these colors from page 1:")
    public void checkGetColorsWithPageRequestParam(DataTable table) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<ColorResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<ColorResponseDto>>() {
                });

        List<ColorResponseDto> expectedColors = extractColors(table);

        assertThat(actual.currentPage())
                .isOne();
        assertThat(actual.lastPage())
                .isOne();
        assertThat(actual.size())
                .isEqualTo(DEFAULT_PAGEABLE_SIZE);
        assertThat(actual.sort())
                .isEqualTo(UNSORTED);

        assertThat(actual.values())
                .containsExactlyInAnyOrderElementsOf(expectedColors);
    }

    private List<ColorResponseDto> extractColors(DataTable table) {
        List<ColorResponseDto> colors = new ArrayList<>();
        List<List<String>> rows = table.asLists(String.class);
        for (List<String> columns : rows) {
            int id = Integer.parseInt(columns.get(0));
            String name = columns.get(1);
            colors.add(ColorResponseDto.builder()
                    .withId(id)
                    .withName(name)
                    .build());
        }
        return colors;
    }
}
