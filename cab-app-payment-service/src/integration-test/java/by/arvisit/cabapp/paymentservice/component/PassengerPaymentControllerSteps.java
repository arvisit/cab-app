package by.arvisit.cabapp.paymentservice.component;

import static by.arvisit.cabapp.paymentservice.util.PaymentITData.URL_PASSENGER_PAYMENTS;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.URL_PASSENGER_PAYMENTS_ID_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getAddedPassengerPaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getListContainerForResponse;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getPassengerPaymentRequestDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getPassengerPaymentResponseDto1;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getPassengerPaymentResponseDto2;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getPassengerPaymentResponseDto3;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getPassengerPaymentResponseDto4;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getPassengerResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getRideResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.common.dto.rides.RideResponseDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.mock.WireMockService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PassengerPaymentControllerSteps {

    private static final String[] BIG_DECIMAL_FIELDS = { "amount", "feeAmount" };
    private static final String ID_FIELD = "id";
    private static final String TIMESTAMP_FIELD = "timestamp";
    private static final String[] FIELDS_FOR_LIST_TO_IGNORE = { "amount", "feeAmount", "timestamp" };
    private static final String VALUES_FIELD = "values";

    @Autowired
    private WireMockService wireMockService;

    private PassengerPaymentRequestDto passengerPaymentRequest;
    private Response response;

    @Given("User wants to save a new passenger payment for the ride with id {string}, passenger id {string}, driver id {string}, amount {int}, payment method {string} and card number {string}")
    public void prepareSaveNewPassengerPaymentRequest(String rideId, String passengerId, String driverId, int amount,
            String paymentMethod, String cardNumber) throws Exception {
        PassengerResponseDto passenger = getPassengerResponseDto().build();
        wireMockService.mockResponseForPassengerClientGetPassengerById(passenger);
        RideResponseDto ride = getRideResponseDto().build();
        wireMockService.mockResponseForRideClientGetRideById(ride);

        passengerPaymentRequest = getPassengerPaymentRequestDto()
                .withRideId(rideId)
                .withPassengerId(passengerId)
                .withDriverId(driverId)
                .withAmount(BigDecimal.valueOf(amount))
                .withPaymentMethod(paymentMethod)
                .withCardNumber(cardNumber)
                .build();
    }

    @When("he performs request to save a new passenger payment")
    public void sendSaveNewPassengerPaymentRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(passengerPaymentRequest)
                .when().post(URL_PASSENGER_PAYMENTS);
    }

    @Then("response should have 201 status, json content type, contain passenger payment with expected parameters and id")
    public void checkSaveNewPassengerPaymentResponse() throws Exception {
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        PassengerPaymentResponseDto actual = response.as(PassengerPaymentResponseDto.class);
        PassengerPaymentResponseDto expected = getAddedPassengerPaymentResponseDto().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, TIMESTAMP_FIELD)
                .isEqualTo(expected);
    }

    @Given("User wants to get details about existing passenger payment")
    public void prepareGetPaymentByIdRequest() {
    }

    @When("he performs request to search passenger payment with id {string}")
    public void sendGetPaymentByIdRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(getPassengerPaymentRequestDto().build())
                .when().get(URL_PASSENGER_PAYMENTS_ID_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain passenger payment with requested id")
    public void checkGetPaymentByIdResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerPaymentResponseDto actual = response.as(PassengerPaymentResponseDto.class);
        PassengerPaymentResponseDto expected = getPassengerPaymentResponseDto1().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELD)
                .isEqualTo(expected);
        assertThat(actual.timestamp())
                .isNotNull();
        assertThat(actual.amount())
                .isEqualByComparingTo(expected.amount());
        assertThat(actual.feeAmount())
                .isEqualByComparingTo(expected.feeAmount());
    }

    @Given("User wants to get details about existing passenger payments")
    public void prepareGetPassengerPaymentsWithNoRequestParamsRequest() {
    }

    @When("he performs request to get passenger payments with no request parameters")
    public void sendGetPassengerPaymentsWithNoRequestParamsRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PASSENGER_PAYMENTS);
    }

    @Then("response should have 200 status, json content type, contain {int} passenger payments")
    public void checkGetPassengerPaymentsWithNoRequestParamsResponse(int paymentsCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PassengerPaymentResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<PassengerPaymentResponseDto>>() {
                });
        ListContainerResponseDto<PassengerPaymentResponseDto> expected = getListContainerForResponse(
                PassengerPaymentResponseDto.class)
                .withValues(List.of(
                        getPassengerPaymentResponseDto1().build(),
                        getPassengerPaymentResponseDto2().build(),
                        getPassengerPaymentResponseDto3().build(),
                        getPassengerPaymentResponseDto4().build()))
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(actual.values())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(FIELDS_FOR_LIST_TO_IGNORE)
                .containsExactlyInAnyOrderElementsOf(expected.values())
                .hasSize(paymentsCount);
    }

    @Given("User wants to get details about existing passenger payments filtered by cash payment method")
    public void prepareGetPassengerPaymentsFilteredByCashPaymentMethodRequest() {
    }

    @When("he performs request to get passenger payments with request parameter {string}={string}")
    public void sendGetPassengerPaymentsFilteredByCashPaymentMethodRequest(String paymentMethodParam,
            String paymentMethodValue) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(paymentMethodParam, paymentMethodValue)
                .when().get(URL_PASSENGER_PAYMENTS);
    }

    @Then("response should have 200 status, json content type, contain {int} filtered passenger payments")
    public void checkGetPassengerPaymentsFilteredByCashPaymentMethodResponse(int paymentsCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PassengerPaymentResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<PassengerPaymentResponseDto>>() {
                });
        ListContainerResponseDto<PassengerPaymentResponseDto> expected = getListContainerForResponse(
                PassengerPaymentResponseDto.class)
                .withValues(List.of(
                        getPassengerPaymentResponseDto3().build(),
                        getPassengerPaymentResponseDto4().build()))
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(actual.values())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(FIELDS_FOR_LIST_TO_IGNORE)
                .containsExactlyInAnyOrderElementsOf(expected.values())
                .hasSize(paymentsCount);
    }
}
