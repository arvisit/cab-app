package by.arvisit.cabapp.ridesservice.contract;

import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.BEGAN_BANK_CARD_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.BOOKED_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DEFAULT_PASSENGER_PASSWORD;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DRIVER_1_EMAIL;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DEFAULT_DRIVER_PASSWORD;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DRIVER_1_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DRIVER_2_EMAIL;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DRIVER_3_EMAIL;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DRIVER_ID_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.ENDED_CASH_NOT_PAID_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.PASSENGER_5_EMAIL;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_ACCEPT_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_END_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getAddedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getBeganBankCardRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getBookedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getEndedCashNotPaidRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getNewRideRequestDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

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
import org.springframework.test.context.jdbc.SqlGroup;

import by.arvisit.cabapp.ridesservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.ridesservice.KeycloakTestContainerExtension;
import by.arvisit.cabapp.ridesservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.RideStatusEnum;
import by.arvisit.cabapp.ridesservice.util.KeycloakIntegrationTestAuth;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("contract")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@ExtendWith(KeycloakTestContainerExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-promo-codes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/add-rides.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-rides.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-promo-codes.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
@AutoConfigureStubRunner(stubsMode = StubsMode.LOCAL,
        ids = { "by.arvisit:cab-app-passenger-service:+:stubs:8480",
                "by.arvisit:cab-app-driver-service:+:stubs:8481",
                "by.arvisit:cab-app-payment-service:+:stubs:8482" })
class RideControllerIntegrationTest extends KeycloakIntegrationTestAuth {

    private static final String[] TIMESTAMP_FIELDS = { "bookRide", "cancelRide", "acceptRide", "beginRide", "endRide",
            "finishRide" };
    private static final String[] BIG_DECIMAL_FIELDS = { "initialCost", "finalCost" };

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn201AndExpectedResponse_whenSaveRide() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(getAuthenticationHeader(PASSENGER_5_EMAIL, DEFAULT_PASSENGER_PASSWORD))
                .body(getNewRideRequestDto().build())
                .when().post(URL_RIDES);

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getAddedRideResponseDto().build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "initialCost", "finalCost", "bookRide")
                .isEqualTo(expected);
        assertThat(result.id())
                .isNotNull();
        assertThat(result.initialCost())
                .isNotNull();
        assertThat(result.finalCost())
                .isNotNull();
        assertThat(result.bookRide())
                .isNotNull();
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenEndRidePaidWithBankCard() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(getAuthenticationHeader(DRIVER_2_EMAIL, DEFAULT_DRIVER_PASSWORD))
                .when().patch(URL_RIDES_ID_END_TEMPLATE, BEGAN_BANK_CARD_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getBeganBankCardRideResponseDto()
                .withStatus(RideStatusEnum.END_RIDE.toString())
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(result.endRide())
                .isNotNull();
        assertThat(result.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(result.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenAcceptRide() {

        String driverId = DRIVER_1_ID;
        Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, driverId);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(getAuthenticationHeader(DRIVER_1_EMAIL, DEFAULT_DRIVER_PASSWORD))
                .body(requestDto)
                .when().patch(URL_RIDES_ID_ACCEPT_TEMPLATE, BOOKED_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getBookedRideResponseDto()
                .withStatus(RideStatusEnum.ACCEPTED.toString())
                .withDriverId(driverId)
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(result.acceptRide())
                .isNotNull();
        assertThat(result.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(result.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenConfirmPaymentCash() throws Exception {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(getAuthenticationHeader(DRIVER_3_EMAIL, DEFAULT_DRIVER_PASSWORD))
                .when().patch(URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE, ENDED_CASH_NOT_PAID_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getEndedCashNotPaidRideResponseDto()
                .withStatus(RideStatusEnum.FINISHED.toString())
                .withIsPaid(true)
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(result.finishRide())
                .isNotNull();
        assertThat(result.isPaid())
                .isTrue();
        assertThat(result.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(result.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }
}
