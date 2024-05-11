package by.arvisit.cabapp.driverservice.component;

import static by.arvisit.cabapp.driverservice.util.DriverITData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.driverservice.util.DriverITData.JOHN_DOE_ID_STRING;
import static by.arvisit.cabapp.driverservice.util.DriverITData.UNSORTED;
import static by.arvisit.cabapp.driverservice.util.DriverITData.URL_AVAILABLE_DRIVERS;
import static by.arvisit.cabapp.driverservice.util.DriverITData.URL_DRIVERS;
import static by.arvisit.cabapp.driverservice.util.DriverITData.URL_DRIVERS_EMAIL_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverITData.URL_DRIVERS_ID_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getAddedDriverResponse;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getJaneDoe;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getJannyDoe;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getJohnDoe;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getJohnnyDoe;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getListContainerForResponse;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getNewCarRequest;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getSaveDriverRequest;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getUpdateDriverRequest;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getUpdatedDriverResponse;
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

    private DriverRequestDto saveNewDriverRequest;

    @Given("User wants to save a new driver with name {string}, email {string}, card number {string} and car details: color id {int}, manufacturer id {int}, registration number {string}")
    public void prepareNewDriverToSave(String name, String email, String cardNumber, int colorId, int manufacturerId,
            String registrationNumber) {
        CarRequestDto car = getNewCarRequest()
                .withColorId(colorId)
                .withManufacturerId(manufacturerId)
                .withRegistrationNumber(registrationNumber)
                .build();
        saveNewDriverRequest = getSaveDriverRequest()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .withCar(car)
                .build();
    }

    private Response saveNewDriverResponse;

    @When("he performs saving via request")
    public void sendSaveNewDriverRequest() {
        saveNewDriverResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(saveNewDriverRequest)
                .when().post(URL_DRIVERS);
    }

    @Then("response should have 201 status, json content type, contain driver with expected parameters and id")
    public void checkSaveNewDriverResponse() {
        saveNewDriverResponse.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        DriverResponseDto actual = saveNewDriverResponse.as(DriverResponseDto.class);
        DriverResponseDto expected = getAddedDriverResponse().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, CAR_ID_FIELD)
                .isEqualTo(expected);
        assertThat(actual.id())
                .isNotNull();
    }

    private DriverRequestDto updateDriverRequest;

    @Given("User wants to update an existing driver with new values for name {string}, email {string}, card number {string} and car details: color id {int}, manufacturer id {int}, registration number {string}")
    public void prepareUpdateRequest(String name, String email, String cardNumber, int colorId, int manufacturerId,
            String registrationNumber) {
        CarRequestDto car = getNewCarRequest()
                .withColorId(colorId)
                .withManufacturerId(manufacturerId)
                .withRegistrationNumber(registrationNumber)
                .build();
        updateDriverRequest = getUpdateDriverRequest()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .withCar(car)
                .build();
    }

    private Response updateDriverResponse;

    @When("he performs update of existing driver with id {string} via request")
    public void sendUpdateDriverRequest(String id) {
        updateDriverResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updateDriverRequest)
                .when().put(URL_DRIVERS_ID_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain driver with updated parameters")
    public void checkUpdateDriverResponse() {
        updateDriverResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverResponseDto actual = updateDriverResponse.as(DriverResponseDto.class);
        DriverResponseDto expected = getUpdatedDriverResponse().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, CAR_ID_FIELD)
                .isEqualTo(expected);
    }

    private String deleteDriverId;
    private List<Driver> driversBeforeDelete;

    @Given("User wants to delete an existing driver with id {string}")
    public void prepareInfoForDriverDelete(String id) {
        deleteDriverId = id;
        driversBeforeDelete = driverRepository.findAll();
    }

    private Response deleteDriverResponse;

    @When("he performs delete of existing driver via request")
    public void sendDeleteDriverRequest() {
        deleteDriverResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete(URL_DRIVERS_ID_TEMPLATE, deleteDriverId);
    }

    @Then("response should have 204 status, minus one driver in database")
    public void checkDeleteDriverResponse() {
        deleteDriverResponse.then()
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

    private String idToGetDriverBy;

    @Given("User wants to get details about an existing driver with id {string}")
    public void prepareInfoForRetrievingDriverById(String id) {
        idToGetDriverBy = id;
    }

    private Response getDriverByIdResponse;

    @When("he performs search driver by id via request")
    public void sendGetDriverByIdRequest() {
        getDriverByIdResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVERS_ID_TEMPLATE, idToGetDriverBy);
    }

    @Then("response should have 200 status, json content type, contain driver with requested id")
    public void checkGetDriverByIdResponse() {
        getDriverByIdResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverResponseDto actual = getDriverByIdResponse.as(DriverResponseDto.class);
        DriverResponseDto expected = getJohnDoe().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private String emailToGetDriverBy;

    @Given("User wants to get details about an existing driver with email {string}")
    public void prepareInfoForRetrievingDriverByEmail(String email) {
        emailToGetDriverBy = email;
    }

    private Response getDriverByEmailResponse;

    @When("he performs search driver by email via request")
    public void sendGetDriverByEmailRequest() {
        getDriverByEmailResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVERS_EMAIL_TEMPLATE, emailToGetDriverBy);
    }

    @Then("response should have 200 status, json content type, contain driver with requested email")
    public void checkGetDriverByEmailResponse() {
        getDriverByEmailResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverResponseDto actual = getDriverByEmailResponse.as(DriverResponseDto.class);
        DriverResponseDto expected = getJohnDoe().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to get details about existing drivers")
    public void prepareInfoForRetrievingDrivers() {
    }

    private Response getDriversWithNoRequestParamsResponse;

    @When("he performs request with no request parameters")
    public void sendGetDriversWithNoRequestParamsRequest() {
        getDriversWithNoRequestParamsResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVERS);
    }

    @Then("response should have 200 status, json content type, contain info about {int} drivers")
    public void checkGetDriversWithNoRequestParams(int driversCount) {
        getDriversWithNoRequestParamsResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverResponseDto> actual = getDriversWithNoRequestParamsResponse
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

    private Response getDriversWithNameEmailParamsResponse;

    @When("he performs request with parameters: {string}={string} and {string}={string}")
    public void sendGetDriversWithNameEmailRequestParamsRequest(String nameParam, String nameValue,
            String emailParam, String emailValue) {
        getDriversWithNameEmailParamsResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(nameParam, nameValue)
                .queryParam(emailParam, emailValue)
                .when().get(URL_DRIVERS);
    }

    @Then("response should have 200 status, json content type, contain info about {int} drivers found by name and email")
    public void checkGetDriversWithNameEmailRequestParams(int driversCount) {
        getDriversWithNameEmailParamsResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverResponseDto> actual = getDriversWithNameEmailParamsResponse
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

    private Response getAvailableDriversWithNoRequestParamsResponse;

    @When("he performs request with no request parameters to available drivers url")
    public void sendGetAvailableDriversWithNoRequestParamsRequest() {
        getAvailableDriversWithNoRequestParamsResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_AVAILABLE_DRIVERS);
    }

    @Then("response should have 200 status, json content type, contain info about {int} available drivers")
    public void checkGetAvailableDriversWithNoRequestParams(int driversCount) {
        getAvailableDriversWithNoRequestParamsResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverResponseDto> actual = getAvailableDriversWithNoRequestParamsResponse
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
