package by.arvisit.cabapp.paymentservice.e2e;

import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.DRIVER_WITHDRAWAL_100_ID;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.URL_DRIVER_PAYMENTS;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.URL_DRIVER_PAYMENTS_ID_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getAddedDriverPaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getDriverPaymentRequestDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getDriverWithdrawal100PaymentResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DriverPaymentControllerSteps {

    private static final String AMOUNT_FIELD = "amount";
    private static final String ID_FIELD = "id";
    private static final String TIMESTAMP_FIELD = "timestamp";

    private DriverPaymentRequestDto driverPaymentRequest;
    private Response response;

    @Given("User wants to save a new driver payment for the driver with id {string}, amount {int}, operation type {string} and card number {string}")
    public void prepareSaveNewDriverPaymentRequest(String driverId, int amount, String operation, String cardNumber) {
        driverPaymentRequest = getDriverPaymentRequestDto()
                .withDriverId(driverId)
                .withAmount(BigDecimal.valueOf(amount))
                .withOperation(operation)
                .withCardNumber(cardNumber)
                .build();
    }

    @When("he performs request to save a new driver payment")
    public void sendSaveNewDriverPaymentRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(driverPaymentRequest)
                .when().post(URL_DRIVER_PAYMENTS);
    }

    @Then("response should have 201 status, json content type, contain driver payment with expected parameters and id")
    public void checkSaveNewDriverPaymentResponse() {
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        DriverPaymentResponseDto actual = response.as(DriverPaymentResponseDto.class);
        DriverPaymentResponseDto expected = getAddedDriverPaymentResponseDto().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, TIMESTAMP_FIELD, AMOUNT_FIELD)
                .isEqualTo(expected);
        assertThat(actual.amount())
                .isEqualByComparingTo(expected.amount());
    }

    @Given("User wants to save a new withdrawal")
    public void prepareSaveNewWithdrawalRequest() {
        driverPaymentRequest = getDriverPaymentRequestDto()
                .withOperation(OperationTypeEnum.WITHDRAWAL.toString())
                .build();
    }

    @Then("driver should have balance greater or equal {int}")
    public void checkBalanceAfterOperation(int expectedBalanceValue) {
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        DriverPaymentResponseDto payment = response.as(DriverPaymentResponseDto.class);

        BigDecimal actual = getDriverBalance(payment.driverId());
        BigDecimal expected = BigDecimal.valueOf(expectedBalanceValue);

        assertThat(actual)
                .isGreaterThanOrEqualTo(expected);
    }

    @Given("User wants to save a new repayment")
    public void prepareSaveNewRepaymentRequest() {
        driverPaymentRequest = getDriverPaymentRequestDto()
                .withOperation(OperationTypeEnum.REPAYMENT.toString())
                .build();
    }

    @Given("User wants to get details about existing driver payment")
    public void prepareGetPaymentByIdRequest() {
    }

    @When("he performs request to search driver payment with id {string}")
    public void sendGetPaymentByIdRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVER_PAYMENTS_ID_TEMPLATE, DRIVER_WITHDRAWAL_100_ID);
    }

    @Then("response should have 200 status, json content type, contain driver payment with requested id")
    public void checkGetPaymentByIdResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverPaymentResponseDto actual = response.as(DriverPaymentResponseDto.class);
        DriverPaymentResponseDto expected = getDriverWithdrawal100PaymentResponseDto().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(AMOUNT_FIELD, TIMESTAMP_FIELD)
                .isEqualTo(expected);
        assertThat(actual.timestamp())
                .isNotNull();
        assertThat(actual.amount())
                .isEqualByComparingTo(expected.amount());
    }

    @Given("User wants to get details about existing driver payments")
    public void prepareGetDriverPaymentsWithNoRequestParamsRequest() {
    }

    @When("he performs request to get driver payments with no request parameters")
    public void sendGetDriverPaymentsWithNoRequestParamsRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVER_PAYMENTS);
    }

    @Then("response should have 200 status, json content type, contain at least {int} driver payments")
    public void checkGetDriverPaymentsResponse(int paymentsCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverPaymentResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<DriverPaymentResponseDto>>() {
                });

        assertThat(actual.values())
                .hasSizeGreaterThanOrEqualTo(paymentsCount);
    }

    @Given("User wants to get details about existing driver payments filtered by driver id and repayment operation type")
    public void prepareGetDriverPaymentsFilteredByDriverIdAndOperationTypeRequest() {
    }

    @When("he performs request to get driver payments with request parameters {string}={string} and {string}={string}")
    public void prepareGetDriverPaymentsFilteredByDriverIdAndOperationTypeRequest(String driverIdParam,
            String driverIdValue, String operationParam, String operationValue) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(operationParam, operationValue)
                .queryParam(driverIdParam, driverIdValue)
                .when().get(URL_DRIVER_PAYMENTS);
    }

    @Given("User wants to get account balance for existing driver")
    public void prepareGetAccountBalanceRequest() {
    }

    @When("he performs request to get account balance for the driver with id {string}")
    public void sendGetAccountBalanceRequest(String driverId) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE, driverId);
    }

    @Then("response should have 200 status, json content type, contain positive account balance")
    public void checkGetAccountBalanceResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverAccountBalanceResponseDto actual = response.as(DriverAccountBalanceResponseDto.class);

        assertThat(actual.balance())
                .isGreaterThan(BigDecimal.ZERO);
    }

    private BigDecimal getDriverBalance(String driverId) {
        Response tmpResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE, driverId);

        DriverAccountBalanceResponseDto accountBalance = tmpResponse.as(DriverAccountBalanceResponseDto.class);

        return accountBalance.balance();
    }
}
