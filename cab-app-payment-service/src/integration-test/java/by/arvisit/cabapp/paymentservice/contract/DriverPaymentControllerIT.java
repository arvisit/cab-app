package by.arvisit.cabapp.paymentservice.contract;

import static by.arvisit.cabapp.paymentservice.util.PaymentITData.DRIVER_ACCOUNT_BALANCE_AFTER_REPAYMENT;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.DRIVER_ACCOUNT_BALANCE_AFTER_WITHDRAWAL;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.URL_DRIVER_PAYMENTS;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getAddedDriverPaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getDriverPaymentRequestDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

import by.arvisit.cabapp.paymentservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.paymentservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum;
import by.arvisit.cabapp.paymentservice.persistence.repository.DriverPaymentRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("contract")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-passenger-payments.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/add-driver-payments.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-driver-payments.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-passenger-payments.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
@AutoConfigureStubRunner(stubsMode = StubsMode.LOCAL,
        ids = { "by.arvisit:cab-app-driver-service:+:stubs:8481" })
class DriverPaymentControllerIT {

    private static final String AMOUNT_FIELD = "amount";
    private static final String ID_FIELD = "id";
    private static final String TIMESTAMP_FIELD = "timestamp";

    @LocalServerPort
    private int serverPort;
    @Autowired
    private DriverPaymentRepository driverPaymentRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn201AndExpectedResponse_whenSaveWithValidInput() throws Exception {

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
                UUID.fromString(requestDto.driverId()));
        BigDecimal expected = DRIVER_ACCOUNT_BALANCE_AFTER_WITHDRAWAL;

        assertThat(actual)
                .isEqualByComparingTo(expected);
    }

    @Test
    void shouldReturn201AndExpectedBalance_whenSaveRepayment() throws Exception {

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
                UUID.fromString(requestDto.driverId()));
        BigDecimal expected = DRIVER_ACCOUNT_BALANCE_AFTER_REPAYMENT;

        assertThat(actual)
                .isEqualByComparingTo(expected);
    }
}
