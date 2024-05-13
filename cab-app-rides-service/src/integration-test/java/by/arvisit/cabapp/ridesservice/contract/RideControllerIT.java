package by.arvisit.cabapp.ridesservice.contract;

import static by.arvisit.cabapp.ridesservice.util.RideITData.BEGAN_BANK_CARD_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_END_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getAddedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getBeganBankCardRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getNewRideRequestDto;
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
import org.springframework.test.context.jdbc.SqlGroup;

import by.arvisit.cabapp.ridesservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.ridesservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.RideStatusEnum;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-promo-codes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/add-rides.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-rides.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-promo-codes.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
@AutoConfigureStubRunner(stubsMode = StubsMode.LOCAL,
        ids = { "by.arvisit:cab-app-passenger-service:0.0.1-SNAPSHOT:stubs:8480" })
class RideControllerIT {

    private static final String PASSENGER_ID_REQUEST_PARAM = "passengerId";
    private static final String[] TIMESTAMP_FIELDS = { "bookRide", "cancelRide", "acceptRide", "beginRide", "endRide",
            "finishRide" };
    private static final String[] BIG_DECIMAL_FIELDS = { "initialCost", "finalCost" };
    private static final String[] FIELDS_FOR_LIST_TO_IGNORE = { "initialCost", "finalCost", "bookRide", "cancelRide",
            "acceptRide", "beginRide", "endRide", "finishRide" };
    private static final String VALUES_FIELD = "values";

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn201AndExpectedResponse_whenSaveRide() throws Exception {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
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
    void shouldReturn200AndExpectedResponse_whenEndRidePaidWithBankCard() throws Exception {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
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
}
