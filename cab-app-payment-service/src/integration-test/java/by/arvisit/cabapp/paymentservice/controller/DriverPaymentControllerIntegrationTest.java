package by.arvisit.cabapp.paymentservice.controller;

import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.DRIVER_ACCOUNT_BALANCE_AFTER_REPAYMENT;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.DRIVER_ACCOUNT_BALANCE_AFTER_WITHDRAWAL;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.DRIVER_ID_REQUEST_PARAM;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.DRIVER_ID_STRING;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.DRIVER_WITHDRAWAL_100_ID;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.OPERATION_REQUEST_PARAM;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.URL_DRIVER_PAYMENTS;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.URL_DRIVER_PAYMENTS_ID_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getAddedDriverPaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getDriverAccountBalanceResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getDriverPaymentRequestDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getDriverRepayment10PaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getDriverResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getDriverWithdrawal100PaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getDriverWithdrawal50PaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getListContainerForResponse;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.paymentservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.paymentservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.mock.WireMockService;
import by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum;
import by.arvisit.cabapp.paymentservice.persistence.repository.DriverPaymentRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-passenger-payments.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/add-driver-payments.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-driver-payments.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-passenger-payments.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
@WireMockTest(httpPort = 8480)
class DriverPaymentControllerIntegrationTest {

    private static final String BALANCE_FIELD = "balance";
    private static final String AMOUNT_FIELD = "amount";
    private static final String ID_FIELD = "id";
    private static final String TIMESTAMP_FIELD = "timestamp";
    private static final String[] FIELDS_FOR_LIST_TO_IGNORE = { "amount", "timestamp" };
    private static final String VALUES_FIELD = "values";

    @LocalServerPort
    private int serverPort;
    @Autowired
    private WireMockService wireMockService;
    @Autowired
    private DriverPaymentRepository driverPaymentRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn201AndExpectedResponse_whenSaveWithValidInput() throws Exception {
        DriverResponseDto driver = getDriverResponseDto().build();
        wireMockService.mockResponseForDriverClientGetDriverById(driver);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(getDriverPaymentRequestDto().build())
                .when().post(URL_DRIVER_PAYMENTS);

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

    @Test
    void shouldReturn201AndExpectedBalance_whenSaveWithdrawal() throws Exception {
        DriverResponseDto driver = getDriverResponseDto().build();
        wireMockService.mockResponseForDriverClientGetDriverById(driver);

        DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto()
                .withOperation(OperationTypeEnum.WITHDRAWAL.toString())
                .build();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().post(URL_DRIVER_PAYMENTS);

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        BigDecimal actual = driverPaymentRepository.getDriverAccountBalance(
                UUID.fromString(driver.id()));
        BigDecimal expected = DRIVER_ACCOUNT_BALANCE_AFTER_WITHDRAWAL;

        assertThat(actual)
                .isEqualByComparingTo(expected);
    }

    @Test
    void shouldReturn201AndExpectedBalance_whenSaveRepayment() throws Exception {
        DriverResponseDto driver = getDriverResponseDto().build();
        wireMockService.mockResponseForDriverClientGetDriverById(driver);

        DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto()
                .withOperation(OperationTypeEnum.REPAYMENT.toString())
                .build();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().post(URL_DRIVER_PAYMENTS);

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        BigDecimal actual = driverPaymentRepository.getDriverAccountBalance(
                UUID.fromString(driver.id()));
        BigDecimal expected = DRIVER_ACCOUNT_BALANCE_AFTER_REPAYMENT;

        assertThat(actual)
                .isEqualByComparingTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPaymentByIdWithExistingId() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVER_PAYMENTS_ID_TEMPLATE, DRIVER_WITHDRAWAL_100_ID);

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

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPaymentsWithNoRequestParams() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVER_PAYMENTS);

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
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPaymentsWithDriverIdAndOperationTypeRequestParam() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(OPERATION_REQUEST_PARAM, OperationTypeEnum.REPAYMENT.toString())
                .queryParam(DRIVER_ID_REQUEST_PARAM, DRIVER_ID_STRING)
                .when().get(URL_DRIVER_PAYMENTS);

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
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetDriverAccountBalanceWithExistingId() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE, DRIVER_ID_STRING);

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
