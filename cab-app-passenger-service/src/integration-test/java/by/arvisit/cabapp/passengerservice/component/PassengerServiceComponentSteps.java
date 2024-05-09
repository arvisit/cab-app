package by.arvisit.cabapp.passengerservice.component;

import static by.arvisit.cabapp.passengerservice.util.PassengerITData.JOHN_DOE_ID_STRING;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.URL_PASSENGERS;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.URL_PASSENGERS_EMAIL_TEMPLATE;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.URL_PASSENGERS_ID_TEMPLATE;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getAddedPassengerResponse;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getUpdatedPassengerResponse;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;
import by.arvisit.cabapp.passengerservice.persistence.repository.PassengerRepository;
import by.arvisit.cabapp.passengerservice.util.PassengerITData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@CucumberContextConfiguration
@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-passengers.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-passengers.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
public class PassengerServiceComponentSteps {

    private static final String ID_FIELD = "id";

    @LocalServerPort
    private int serverPort;
    @Autowired
    private PassengerRepository passengerRepository;

    private PassengerRequestDto saveNewPassengerRequest;
    private Response saveNewPassengerResponse;
    private PassengerRequestDto updatePassengerRequest;
    private Response updatePassengerResponse;
    private String deletePassengerId;
    private List<Passenger> passengersBeforeDelete;
    private Response deletePassengerResponse;
    private String idToGetPassengerBy;
    private Response getPassengerByIdResponse;
    private String emailToGetPassengerBy;
    private Response getPassengerByEmailResponse;
    private Response getPassengersWithNoRequestParamsResponse;
    private Response getPassengersWithNameEmailParamsResponse;

    @BeforeAll
    public static void setUpDB() {
        System.setProperty("spring.datasource.url", "jdbc:tc:postgresql:15-alpine:///");
    }

    @Before
    public void setUpPort() {
        RestAssured.port = serverPort;
    }

    @Given("User wants to save a new passenger with name {string}, email {string} and card number {string}")
    public void prepareNewPassengerToSave(String name, String email, String cardNumber) {
        saveNewPassengerRequest = PassengerITData.getSavePassengerRequest()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .build();
    }

    @When("he performs saving via request")
    public void sendSaveNewPassengerRequest() {
        saveNewPassengerResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(saveNewPassengerRequest)
                .when().post(URL_PASSENGERS);
    }

    @Then("response should have 201 status, json content type, contain passenger with expected parameters and id")
    public void checkSaveNewPassengerResponse() {
        saveNewPassengerResponse.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto actual = saveNewPassengerResponse.as(PassengerResponseDto.class);
        PassengerResponseDto expected = getAddedPassengerResponse().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD)
                .isEqualTo(expected);
        assertThat(actual.id())
                .isNotNull();
    }

    @Given("User wants to update an existing passenger with new name {string}, email {string} and card number {string} values")
    public void prepareUpdateRequest(String name, String email, String cardNumber) {
        updatePassengerRequest = PassengerITData.getSavePassengerRequest()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .build();
    }

    @When("he performs update of existing passenger with id {string} via request")
    public void sendUpdatePassengerRequest(String id) {
        updatePassengerResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatePassengerRequest)
                .when().put(URL_PASSENGERS_ID_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain passenger with updated parameters")
    public void checkUpdatePassengerResponse() {
        updatePassengerResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto actual = updatePassengerResponse.as(PassengerResponseDto.class);
        PassengerResponseDto expected = getUpdatedPassengerResponse().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to delete an existing passenger with id {string}")
    public void prepareInfoForPassengerDelete(String id) {
        deletePassengerId = id;
    }

    @When("he performs delete of existing passenger via request")
    public void sendDeletePassengerRequest() {
        passengersBeforeDelete = passengerRepository.findAll();

        deletePassengerResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete(URL_PASSENGERS_ID_TEMPLATE, deletePassengerId);
    }

    @Then("response should have 204 status, minus one passenger in database")
    public void checkDeletePassengerResponse() {
        deletePassengerResponse.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        List<Passenger> passengersAfterDelete = passengerRepository.findAll();

        assertThat(passengersAfterDelete).size()
                .isEqualTo(passengersBeforeDelete.size() - 1);

        Predicate<? super Passenger> matchById = p -> p.getId()
                .equals(UUID.fromString(JOHN_DOE_ID_STRING));
        assertThat(passengersBeforeDelete)
                .anyMatch(matchById);
        assertThat(passengersAfterDelete)
                .noneMatch(matchById);
    }

    @Given("User wants to get details about an existing passenger with id {string}")
    public void prepareInfoForRetrievingPassengerById(String id) {
        idToGetPassengerBy = id;
    }

    @When("he performs search passenger by id via request")
    public void sendGetPassengerByIdRequest() {
        getPassengerByIdResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PASSENGERS_ID_TEMPLATE, idToGetPassengerBy);
    }

    @Then("response should have 200 status, json content type, contain passenger with requested id")
    public void checkGetPassengerByIdResponse() {
        getPassengerByIdResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto actual = getPassengerByIdResponse.as(PassengerResponseDto.class);
        PassengerResponseDto expected = PassengerITData.getJohnDoe().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to get details about an existing passenger with email {string}")
    public void prepareInfoForRetrievingPassengerByEmail(String email) {
        emailToGetPassengerBy = email;
    }

    @When("he performs search passenger by email via request")
    public void sendGetPassengerByEmailRequest() {
        getPassengerByEmailResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PASSENGERS_EMAIL_TEMPLATE, emailToGetPassengerBy);
    }

    @Then("response should have 200 status, json content type, contain passenger with requested email")
    public void checkGetPassengerByEmailResponse() {
        getPassengerByEmailResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto actual = getPassengerByEmailResponse.as(PassengerResponseDto.class);
        PassengerResponseDto expected = PassengerITData.getJohnDoe().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to get details about existing passengers")
    public void prepareInfoForRetrievingPassengers() {
    }

    @When("he performs request with no request parameters")
    public void sendGetPassengersWithNoRequestParamsRequest() {
        getPassengersWithNoRequestParamsResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PASSENGERS);
    }

    @Then("response should have 200 status, json content type, contain info about these passengers:")
    public void checkGetPassengersWithNoRequestParams(DataTable table) {
        ListContainerResponseDto<PassengerResponseDto> actual = getPassengersWithNoRequestParamsResponse
                .as(new TypeRef<ListContainerResponseDto<PassengerResponseDto>>() {
                });

        List<PassengerResponseDto> expectedPassengers = extractPassengers(table);

        assertThat(actual.currentPage())
                .isZero();
        assertThat(actual.lastPage())
                .isZero();
        assertThat(actual.size())
                .isEqualTo(PassengerITData.DEFAULT_PAGEABLE_SIZE);
        assertThat(actual.sort())
                .isEqualTo(PassengerITData.UNSORTED);

        assertThat(actual.values())
                .containsExactlyInAnyOrderElementsOf(expectedPassengers);
    }

    @When("he performs request with parameters: {string}={string} and {string}={string}")
    public void sendGetPassengersWithNameEmailRequestParamsRequest(String nameParam, String nameValue,
            String emailParam, String emailValue) {
        getPassengersWithNameEmailParamsResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(nameParam, nameValue)
                .queryParam(emailParam, emailValue)
                .when().get(URL_PASSENGERS);
    }

    @Then("response should have 200 status, json content type, contain info about these passengers found by name and email:")
    public void checkGetPassengersWithNameEmailRequestParams(DataTable table) {
        ListContainerResponseDto<PassengerResponseDto> actual = getPassengersWithNameEmailParamsResponse
                .as(new TypeRef<ListContainerResponseDto<PassengerResponseDto>>() {
                });

        List<PassengerResponseDto> expectedPassengers = extractPassengers(table);

        assertThat(actual.currentPage())
                .isZero();
        assertThat(actual.lastPage())
                .isZero();
        assertThat(actual.size())
                .isEqualTo(PassengerITData.DEFAULT_PAGEABLE_SIZE);
        assertThat(actual.sort())
                .isEqualTo(PassengerITData.UNSORTED);

        assertThat(actual.values())
                .containsExactlyInAnyOrderElementsOf(expectedPassengers);
    }

    private List<PassengerResponseDto> extractPassengers(DataTable table) {
        List<PassengerResponseDto> passengers = new ArrayList<>();
        List<List<String>> rows = table.asLists(String.class);
        for (List<String> columns : rows) {
            String id = columns.get(0);
            String name = columns.get(1);
            String email = columns.get(2);
            String cardNumber = "null".equals(columns.get(3)) ? null : columns.get(3);
            passengers.add(PassengerResponseDto.builder()
                    .withId(id)
                    .withName(name)
                    .withEmail(email)
                    .withCardNumber(cardNumber)
                    .build());
        }
        return passengers;
    }
}
