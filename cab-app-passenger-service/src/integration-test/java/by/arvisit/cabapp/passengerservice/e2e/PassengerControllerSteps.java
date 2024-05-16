package by.arvisit.cabapp.passengerservice.e2e;

import static by.arvisit.cabapp.passengerservice.util.PassengerITData.JOHN_DOE_ID_STRING;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.URL_PASSENGERS;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.URL_PASSENGERS_EMAIL_TEMPLATE;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.URL_PASSENGERS_ID_TEMPLATE;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getAddedPassengerResponse;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getUpdatedPassengerResponse;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.util.PassengerITData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PassengerControllerSteps {

    private static final String PAGE_REQUEST_PARAM = "page";

    private static final String ID_FIELD = "id";

    private PassengerRequestDto passengerRequest;
    private Response response;
    private String passengerId;
    private List<PassengerResponseDto> passengersBeforeDelete;
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

    @Then("response should have 200 status, json content type, contain passenger with name {string}, email {string} and card number {string}")
    public void checkUpdatePassengerResponse(String name, String email, String cardNumber) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto actual = response.as(PassengerResponseDto.class);
        PassengerResponseDto expected = getUpdatedPassengerResponse()
                .withName(name)
                .withEmail(email)
                .withCardNumber(cardNumber)
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to delete an existing passenger with id {string}")
    public void prepareInfoForPassengerDelete(String id) {
        passengerId = id;
        passengersBeforeDelete = extractAllItems();
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

        List<PassengerResponseDto> passengersAfterDelete = extractAllItems();

        assertThat(passengersAfterDelete).size()
                .isEqualTo(passengersBeforeDelete.size() - 1);

        Predicate<? super PassengerResponseDto> matchById = p -> p.id()
                .equals(JOHN_DOE_ID_STRING);
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
        PassengerResponseDto expected = PassengerITData.getJohnnyDoe().build();

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
        PassengerResponseDto expected = PassengerITData.getJohnnyDoe().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private List<PassengerResponseDto> extractAllItems() {
        List<PassengerResponseDto> items = new ArrayList<>();

        int nextPage = 0;
        int lastPage = 0;

        do {
            Response tmpResponse = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .queryParam(PAGE_REQUEST_PARAM, nextPage)
                    .when().get(URL_PASSENGERS);
            ListContainerResponseDto<PassengerResponseDto> itemsContainer = tmpResponse
                    .as(new TypeRef<ListContainerResponseDto<PassengerResponseDto>>() {
                    });
            items.addAll(itemsContainer.values());
            lastPage = itemsContainer.lastPage();
            nextPage++;
        } while (nextPage <= lastPage);

        return items;
    }
}
