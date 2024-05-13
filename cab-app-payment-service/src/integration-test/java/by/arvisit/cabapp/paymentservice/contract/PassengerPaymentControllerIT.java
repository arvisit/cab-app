package by.arvisit.cabapp.paymentservice.contract;

import static by.arvisit.cabapp.paymentservice.util.PaymentITData.URL_PASSENGER_PAYMENTS;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getAddedPassengerPaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentITData.getPassengerPaymentRequestDto;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("contract")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-passenger-payments.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-passenger-payments.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
@AutoConfigureStubRunner(stubsMode = StubsMode.LOCAL,
        ids = { "by.arvisit:cab-app-passenger-service:+:stubs:8480",
                "by.arvisit:cab-app-rides-service:+:stubs:8482" })
class PassengerPaymentControllerIT {

    private static final String ID_FIELD = "id";
    private static final String TIMESTAMP_FIELD = "timestamp";

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn201AndExpectedResponse_whenSaveWithValidInput() throws Exception {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(getPassengerPaymentRequestDto().build())
                .when().post(URL_PASSENGER_PAYMENTS);

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
}
