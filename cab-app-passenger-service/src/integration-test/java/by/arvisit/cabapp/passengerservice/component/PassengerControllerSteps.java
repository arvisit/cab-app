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
import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;
import by.arvisit.cabapp.passengerservice.persistence.repository.PassengerRepository;
import by.arvisit.cabapp.passengerservice.util.PassengerITData;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PassengerControllerSteps {

    private static final String ID_FIELD = "id";

    @Autowired
    private PassengerRepository passengerRepository;

    private PassengerRequestDto passengerRequest;
    private Response response;
    private String passengerId;
    private List<Passenger> passengersBeforeDelete;
    private String emailToGetPassengerBy;

    @Given("User wants to save a new passenger with name {string}, email {string} and card number {string}")
    public void prepareNewPassengerToSave(String name, String email, String cardNumber) {
        passengerRequest = PassengerITData.getSavePassengerRequest()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .build();
    }

    @When("he performs saving via request")
    public void sendSaveNewPassengerRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(passengerRequest)
                .when().post(URL_PASSENGERS);
    }

    @Then("response should have 201 status, json content type, contain passenger with expected parameters and id")
    public void checkSaveNewPassengerResponse() {
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto actual = response.as(PassengerResponseDto.class);
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
        passengerRequest = PassengerITData.getSavePassengerRequest()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .build();
    }

    @When("he performs update of existing passenger with id {string} via request")
    public void sendUpdatePassengerRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(passengerRequest)
                .when().put(URL_PASSENGERS_ID_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain passenger with updated parameters")
    public void checkUpdatePassengerResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto actual = response.as(PassengerResponseDto.class);
        PassengerResponseDto expected = getUpdatedPassengerResponse().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to delete an existing passenger with id {string}")
    public void prepareInfoForPassengerDelete(String id) {
        passengerId = id;
        passengersBeforeDelete = passengerRepository.findAll();
    }

    @When("he performs delete of existing passenger via request")
    public void sendDeletePassengerRequest() {

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete(URL_PASSENGERS_ID_TEMPLATE, passengerId);
    }

    @Then("response should have 204 status, minus one passenger in database")
    public void checkDeletePassengerResponse() {
        response.then()
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
        passengerId = id;
    }

    @When("he performs search passenger by id via request")
    public void sendGetPassengerByIdRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PASSENGERS_ID_TEMPLATE, passengerId);
    }

    @Then("response should have 200 status, json content type, contain passenger with requested id")
    public void checkGetPassengerByIdResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto actual = response.as(PassengerResponseDto.class);
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
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PASSENGERS_EMAIL_TEMPLATE, emailToGetPassengerBy);
    }

    @Then("response should have 200 status, json content type, contain passenger with requested email")
    public void checkGetPassengerByEmailResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto actual = response.as(PassengerResponseDto.class);
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
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PASSENGERS);
    }

    @Then("response should have 200 status, json content type, contain info about these passengers:")
    public void checkGetPassengersWithNoRequestParams(DataTable table) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PassengerResponseDto> actual = response
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
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(nameParam, nameValue)
                .queryParam(emailParam, emailValue)
                .when().get(URL_PASSENGERS);
    }

    @Then("response should have 200 status, json content type, contain info about these passengers found by name and email:")
    public void checkGetPassengersWithNameEmailRequestParams(DataTable table) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PassengerResponseDto> actual = response
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
