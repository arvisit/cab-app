package by.arvisit.cabapp.driverservice.e2e;

import static by.arvisit.cabapp.driverservice.util.CarManufacturerIntegrationTestData.CAR_MANUFACTURERS;
import static by.arvisit.cabapp.driverservice.util.ColorIntegrationTestData.COLORS;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.JOHN_DOE_ID_STRING;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.SHIFT;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_DRIVERS;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_DRIVERS_EMAIL_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_DRIVERS_ID_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getAddedDriverResponse;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJohnDoeCar;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJohnnyDoe;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getNewCarRequest;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getSaveDriverRequest;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getUpdateDriverRequest;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getUpdatedDriverResponse;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarRequestDto;
import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DriverControllerSteps {

    private static final String PAGE_REQUEST_PARAM = "page";
    private static final String ID_FIELD = "id";
    private static final String CAR_ID_FIELD = "car.id";

    private DriverRequestDto driverRequest;
    private Response response;
    private String driverId;
    private List<DriverResponseDto> driversBeforeDelete;
    private String emailToGetDriverBy;

    @Given("User wants to save a new driver with name {string}, email {string}, card number {string} and car details: color id {int}, manufacturer id {int}, registration number {string}")
    public void prepareNewDriverToSave(String name, String email, String cardNumber, int colorId, int manufacturerId,
            String registrationNumber) {
        CarRequestDto car = getNewCarRequest()
                .withColorId(colorId)
                .withManufacturerId(manufacturerId)
                .withRegistrationNumber(registrationNumber)
                .build();
        driverRequest = getSaveDriverRequest()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .withCar(car)
                .build();
    }

    @When("he performs saving via request")
    public void sendSaveNewDriverRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(driverRequest)
                .when().post(URL_DRIVERS);
    }

    @Then("response should have 201 status, json content type, contain driver with expected parameters and id")
    public void checkSaveNewDriverResponse() {
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        DriverResponseDto actual = response.as(DriverResponseDto.class);
        DriverResponseDto expected = getAddedDriverResponse().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, CAR_ID_FIELD)
                .isEqualTo(expected);
        assertThat(actual.id())
                .isNotNull();
    }

    @Given("User wants to update an existing driver with new values for name {string}, email {string}, card number {string} and car details: color id {int}, manufacturer id {int}, registration number {string}")
    public void prepareUpdateRequest(String name, String email, String cardNumber, int colorId, int manufacturerId,
            String registrationNumber) {
        CarRequestDto car = getNewCarRequest()
                .withColorId(colorId)
                .withManufacturerId(manufacturerId)
                .withRegistrationNumber(registrationNumber)
                .build();
        driverRequest = getUpdateDriverRequest()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .withCar(car)
                .build();
    }

    @When("he performs update of existing driver with id {string} via request")
    public void sendUpdateDriverRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(driverRequest)
                .when().put(URL_DRIVERS_ID_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain driver with name {string}, email {string}, card number {string} and car details: color id {int}, manufacturer id {int}, registration number {string}")
    public void checkUpdateDriverResponse(String name, String email, String cardNumber, int colorId, int manufacturerId,
            String registrationNumber) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverResponseDto actual = response.as(DriverResponseDto.class);
        CarResponseDto expectedCar = getJohnDoeCar()
                .withColor(COLORS.get(colorId - SHIFT))
                .withManufacturer(CAR_MANUFACTURERS.get(manufacturerId - SHIFT))
                .withRegistrationNumber(registrationNumber)
                .build();
        DriverResponseDto expected = getUpdatedDriverResponse()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .withCar(expectedCar)
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, CAR_ID_FIELD)
                .isEqualTo(expected);
    }

    @Given("User wants to delete an existing driver with id {string}")
    public void prepareInfoForDriverDelete(String id) {
        driverId = id;
        driversBeforeDelete = extractAllItems();
    }

    @When("he performs delete of existing driver via request")
    public void sendDeleteDriverRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete(URL_DRIVERS_ID_TEMPLATE, driverId);
    }

    @Then("response should have 204 status, minus one driver in database")
    public void checkDeleteDriverResponse() {
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        List<DriverResponseDto> driversAfterDelete = extractAllItems();

        assertThat(driversAfterDelete).size()
                .isEqualTo(driversBeforeDelete.size() - 1);

        Predicate<? super DriverResponseDto> matchById = p -> p.id()
                .equals(JOHN_DOE_ID_STRING);
        assertThat(driversBeforeDelete)
                .anyMatch(matchById);
        assertThat(driversAfterDelete)
                .noneMatch(matchById);
    }

    @Given("User wants to get details about an existing driver with id {string}")
    public void prepareInfoForRetrievingDriverById(String id) {
        driverId = id;
    }

    @When("he performs search driver by id via request")
    public void sendGetDriverByIdRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVERS_ID_TEMPLATE, driverId);
    }

    @Then("response should have 200 status, json content type, contain driver with requested id")
    public void checkGetDriverByIdResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverResponseDto actual = response.as(DriverResponseDto.class);
        DriverResponseDto expected = getJohnnyDoe().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to get details about an existing driver with email {string}")
    public void prepareInfoForRetrievingDriverByEmail(String email) {
        emailToGetDriverBy = email;
    }

    @When("he performs search driver by email via request")
    public void sendGetDriverByEmailRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVERS_EMAIL_TEMPLATE, emailToGetDriverBy);
    }

    @Then("response should have 200 status, json content type, contain driver with requested email")
    public void checkGetDriverByEmailResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverResponseDto actual = response.as(DriverResponseDto.class);
        DriverResponseDto expected = getJohnnyDoe().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private List<DriverResponseDto> extractAllItems() {
        List<DriverResponseDto> items = new ArrayList<>();

        int nextPage = 0;
        int lastPage = 0;

        do {
            Response tmpResponse = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .queryParam(PAGE_REQUEST_PARAM, nextPage)
                    .when().get(URL_DRIVERS);
            ListContainerResponseDto<DriverResponseDto> itemsContainer = tmpResponse
                    .as(new TypeRef<ListContainerResponseDto<DriverResponseDto>>() {
                    });
            items.addAll(itemsContainer.values());
            lastPage = itemsContainer.lastPage();
            nextPage++;
        } while (nextPage <= lastPage);

        return items;
    }
}
