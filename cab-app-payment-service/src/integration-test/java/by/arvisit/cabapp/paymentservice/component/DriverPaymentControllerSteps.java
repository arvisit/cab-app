package by.arvisit.cabapp.paymentservice.component;

import static by.arvisit.cabapp.paymentservice.util.PaymentITData.DRIVER_WITHDRAWAL_100_ID;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.URL_DRIVER_PAYMENTS;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.URL_DRIVER_PAYMENTS_ID_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getAddedDriverPaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getDriverAccountBalanceResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getDriverPaymentRequestDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getDriverRepayment10PaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getDriverResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getDriverWithdrawal100PaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getDriverWithdrawal50PaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getListContainerForResponse;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.mock.WireMockService;
import by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum;
import by.arvisit.cabapp.paymentservice.persistence.repository.DriverPaymentRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DriverPaymentControllerSteps {

    private static final String BALANCE_FIELD = "balance";
    private static final String AMOUNT_FIELD = "amount";
    private static final String ID_FIELD = "id";
    private static final String TIMESTAMP_FIELD = "timestamp";
    private static final String[] FIELDS_FOR_LIST_TO_IGNORE = { "amount", "timestamp" };
    private static final String VALUES_FIELD = "values";

    @Autowired
    private WireMockService wireMockService;
    @Autowired
    private DriverPaymentRepository driverPaymentRepository;

    private DriverPaymentRequestDto driverPaymentRequest;
    private Response response;

    @Given("User wants to save a new driver payment for the driver with id {string}, amount {int}, operation type {string} and card number {string}")
    public void prepareSaveNewDriverPaymentRequest(String driverId, int amount, String operation, String cardNumber)
            throws Exception {
        DriverResponseDto driver = getDriverResponseDto().build();
        wireMockService.mockResponseForDriverClientGetDriverById(driver);
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
    public void prepareSaveNewWithdrawalRequest() throws Exception {
        DriverResponseDto driver = getDriverResponseDto().build();
        wireMockService.mockResponseForDriverClientGetDriverById(driver);

        driverPaymentRequest = getDriverPaymentRequestDto()
                .withOperation(OperationTypeEnum.WITHDRAWAL.toString())
                .build();
    }

    @Then("driver should have balance of {int} after that")
    public void checkBalanceAfterOperation(int expectedBalanceValue) {
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        DriverPaymentResponseDto payment = response.as(DriverPaymentResponseDto.class);

        BigDecimal actual = driverPaymentRepository.getDriverAccountBalance(
                UUID.fromString(payment.driverId()));
        BigDecimal expected = BigDecimal.valueOf(expectedBalanceValue);

        assertThat(actual)
                .isEqualByComparingTo(expected);
    }

    @Given("User wants to save a new repayment")
    public void prepareSaveNewRepaymentRequest() throws Exception {
        DriverResponseDto driver = getDriverResponseDto().build();
        wireMockService.mockResponseForDriverClientGetDriverById(driver);

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

    @Then("response should have 200 status, json content type, contain {int} driver payments")
    public void checkGetDriverPaymentsWithNoRequestParamsResponse(int paymentsCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverPaymentResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<DriverPaymentResponseDto>>() {
                });
        ListContainerResponseDto<DriverPaymentResponseDto> expected = getListContainerForResponse(
                DriverPaymentResponseDto.class)
                .withValues(List.of(
                        getDriverWithdrawal100PaymentResponseDto().build(),
                        getDriverWithdrawal50PaymentResponseDto().build(),
                        getDriverRepayment10PaymentResponseDto().build()))
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

    @Then("response should have 200 status, json content type, contain {int} filtered driver payment")
    public void checkGetDriverPaymentsFilteredByDriverIdAndOperationTypeResponse(int paymentsCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverPaymentResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<DriverPaymentResponseDto>>() {
                });
        ListContainerResponseDto<DriverPaymentResponseDto> expected = getListContainerForResponse(
                DriverPaymentResponseDto.class)
                .withValues(List.of(
                        getDriverRepayment10PaymentResponseDto().build()))
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

    @Given("User wants to get account balance for existing driver")
    public void prepareGetAccountBalanceRequest() {
    }

    @When("he performs request to get account balance for the driver with id {string}")
    public void sendGetAccountBalanceRequest(String driverId) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE, driverId);
    }

    @Then("response should have 200 status, json content type, contain account balance with expected parameters")
    public void checkGetAccountBalanceResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverAccountBalanceResponseDto actual = response.as(DriverAccountBalanceResponseDto.class);
        DriverAccountBalanceResponseDto expected = getDriverAccountBalanceResponseDto().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BALANCE_FIELD)
                .isEqualTo(expected);
        assertThat(actual.balance())
                .isEqualByComparingTo(expected.balance());
    }
}
