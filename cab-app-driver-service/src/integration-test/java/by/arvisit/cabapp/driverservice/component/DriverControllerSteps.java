package by.arvisit.cabapp.driverservice.component;

import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.JOHN_DOE_ID_STRING;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.UNSORTED;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_AVAILABLE_DRIVERS;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_DRIVERS;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_DRIVERS_EMAIL_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_DRIVERS_ID_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getAddedDriverResponse;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJaneDoe;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJannyDoe;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJohnDoe;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJohnnyDoe;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getListContainerForResponse;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getNewCarRequest;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getSaveDriverRequest;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getUpdateDriverRequest;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getUpdatedDriverResponse;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Driver;
import by.arvisit.cabapp.driverservice.persistence.repository.DriverRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DriverControllerSteps {

    private static final String ID_FIELD = "id";
    private static final String CAR_ID_FIELD = "car.id";

    @Autowired
    private DriverRepository driverRepository;

    private DriverRequestDto driverRequest;
    private Response response;
    private String driverId;
    private List<Driver> driversBeforeDelete;
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

    @Then("response should have 200 status, json content type, contain driver with updated parameters")
    public void checkUpdateDriverResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverResponseDto actual = response.as(DriverResponseDto.class);
        DriverResponseDto expected = getUpdatedDriverResponse().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, CAR_ID_FIELD)
                .isEqualTo(expected);
    }

    @Given("User wants to delete an existing driver with id {string}")
    public void prepareInfoForDriverDelete(String id) {
        driverId = id;
        driversBeforeDelete = driverRepository.findAll();
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

        List<Driver> driversAfterDelete = driverRepository.findAll();

        assertThat(driversAfterDelete).size()
                .isEqualTo(driversBeforeDelete.size() - 1);

        Predicate<? super Driver> matchById = p -> p.getId()
                .equals(UUID.fromString(JOHN_DOE_ID_STRING));
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
        DriverResponseDto expected = getJohnDoe().build();

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
        DriverResponseDto expected = getJohnDoe().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to get details about existing drivers")
    public void prepareInfoForRetrievingDrivers() {
    }

    @When("he performs request with no request parameters")
    public void sendGetDriversWithNoRequestParamsRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVERS);
    }

    @Then("response should have 200 status, json content type, contain info about {int} drivers")
    public void checkGetDriversWithNoRequestParams(int driversCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<DriverResponseDto>>() {
                });

        ListContainerResponseDto<DriverResponseDto> expected = getListContainerForResponse(DriverResponseDto.class)
                .withValues(List.of(getJohnDoe().build(), getJaneDoe().build(),
                        getJohnnyDoe().build(), getJannyDoe().build()))
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
                .hasSize(driversCount);
    }

    @When("he performs request with parameters: {string}={string} and {string}={string}")
    public void sendGetDriversWithNameEmailRequestParamsRequest(String nameParam, String nameValue,
            String emailParam, String emailValue) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(nameParam, nameValue)
                .queryParam(emailParam, emailValue)
                .when().get(URL_DRIVERS);
    }

    @Then("response should have 200 status, json content type, contain info about {int} drivers found by name and email")
    public void checkGetDriversWithNameEmailRequestParams(int driversCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<DriverResponseDto>>() {
                });

        ListContainerResponseDto<DriverResponseDto> expected = getListContainerForResponse(DriverResponseDto.class)
                .withValues(List.of(getJohnDoe().build(), getJaneDoe().build(), getJannyDoe().build()))
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
                .hasSize(driversCount);
    }

    @Given("User wants to get details about available drivers")
    public void prepareInfoForRetrievingAvailableDrivers() {
    }

    @When("he performs request with no request parameters to available drivers url")
    public void sendGetAvailableDriversWithNoRequestParamsRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_AVAILABLE_DRIVERS);
    }

    @Then("response should have 200 status, json content type, contain info about {int} available drivers")
    public void checkGetAvailableDriversWithNoRequestParams(int driversCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<DriverResponseDto>>() {
                });

        ListContainerResponseDto<DriverResponseDto> expected = getListContainerForResponse(DriverResponseDto.class)
                .withValues(List.of(getJohnnyDoe().build(), getJannyDoe().build()))
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
                .hasSize(driversCount);
    }
}
