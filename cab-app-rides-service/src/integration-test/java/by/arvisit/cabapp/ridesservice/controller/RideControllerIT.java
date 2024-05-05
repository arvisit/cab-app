package by.arvisit.cabapp.ridesservice.controller;

import static by.arvisit.cabapp.ridesservice.util.RideITData.ACCEPTED_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.BEGAN_BANK_CARD_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.BEGAN_CASH_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.BOOKED_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.BRILLIANT10_KEYWORD;
import static by.arvisit.cabapp.ridesservice.util.RideITData.DEFAULT_SCORE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.DRIVER_1_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.DRIVER_3_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.DRIVER_4_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.DRIVER_ID_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideITData.DRIVER_SCORE_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideITData.ENDED_BANK_CARD_NOT_PAID_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.ENDED_BANK_CARD_PAID_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.ENDED_CASH_NOT_PAID_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.FINISHED_NO_SCORES_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.PASSENGER_1_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.PASSENGER_3_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.PASSENGER_4_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.PASSENGER_SCORE_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideITData.PAYMENT_METHOD_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideITData.PROMO_CODE_KEYWORD_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideITData.RIDE_FINAL_COST_WITH_BRILLIANT10_PROMO_CODE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_DRIVER_ID_RATING_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_DRIVER_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_ACCEPT_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_APPLY_PROMO_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_BEGIN_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_CANCEL_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_END_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_FINISH_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_SCORE_DRIVER_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_PASSENGER_ID_RATING_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_RIDES_PASSENGER_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getAcceptedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getAddedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getBeganBankCardRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getBeganCashRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getBookedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getDriverRating;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getDriverResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getEndedBankCardNotPaidRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getEndedBankCardPaidRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getEndedCashNotPaidRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getFinishedNoScoresRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getFinishedWithScoresRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getListContainerForResponse;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getNewRideRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getPassengerPaymentResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getPassengerRating;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getPassengerResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

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
import org.springframework.test.context.jdbc.SqlGroup;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;
import by.arvisit.cabapp.ridesservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.ridesservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.ridesservice.dto.RatingResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.mock.WireMockService;
import by.arvisit.cabapp.ridesservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import by.arvisit.cabapp.ridesservice.persistence.model.RideStatusEnum;
import by.arvisit.cabapp.ridesservice.persistence.repository.RideRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
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
@WireMockTest(httpPort = 8480)
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
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private WireMockService wireMockService;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn201AndExpectedResponse_whenSaveRide() throws Exception {

        PassengerResponseDto passenger = getPassengerResponseDto().build();
        wireMockService.mockResponseForPassengerClientGetPassengerById(passenger);

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
    void shouldReturn200AndExpectedResponse_whenCancelRide() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_RIDES_ID_CANCEL_TEMPLATE, BOOKED_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getBookedRideResponseDto()
                .withStatus(RideStatusEnum.CANCELED.toString())
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(result.cancelRide())
                .isNotNull();
        assertThat(result.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(result.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenAcceptRide() throws Exception {

        DriverResponseDto driverBeforeAccept = getDriverResponseDto().build();
        wireMockService.mockResponseForDriverClientGetDriverById(driverBeforeAccept);
        DriverResponseDto driverAfterAccept = getDriverResponseDto()
                .withIsAvailable(false)
                .build();
        wireMockService.mockResponseForDriverClientUpdateAvailability(driverAfterAccept);

        String driverId = DRIVER_1_ID;
        Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, driverId);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
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
    void shouldReturn200AndExpectedResponse_whenBeginRide() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_RIDES_ID_BEGIN_TEMPLATE, ACCEPTED_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getAcceptedRideResponseDto()
                .withStatus(RideStatusEnum.BEGIN_RIDE.toString())
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(result.beginRide())
                .isNotNull();
        assertThat(result.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(result.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenEndRidePaidWithBankCard() throws Exception {

        PassengerResponseDto passenger = getPassengerResponseDto()
                .withId(PASSENGER_3_ID)
                .build();
        wireMockService.mockResponseForPassengerClientGetPassengerById(passenger);

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

    @Test
    void shouldReturn200AndExpectedResponse_whenEndRidePaidWithCash() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_RIDES_ID_END_TEMPLATE, BEGAN_CASH_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getBeganCashRideResponseDto()
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
    void shouldReturn200AndExpectedResponse_whenFinishRide() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_RIDES_ID_FINISH_TEMPLATE, ENDED_BANK_CARD_PAID_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getEndedBankCardPaidRideResponseDto()
                .withStatus(RideStatusEnum.FINISHED.toString())
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(result.finishRide())
                .isNotNull();
        assertThat(result.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(result.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenConfirmPaymentCash() throws Exception {

        PassengerPaymentResponseDto payment = getPassengerPaymentResponseDto().build();
        wireMockService.mockResponseForPaymentClientSave(payment);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
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

    @Test
    void shouldReturn200AndExpectedResponse_whenConfirmPaymentBankCard() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE, ENDED_BANK_CARD_NOT_PAID_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getEndedBankCardNotPaidRideResponseDto()
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

    @Test
    void shouldReturn200AndExpectedResponse_whenApplyPromoCode() {

        String promoCodeKeyword = BRILLIANT10_KEYWORD;
        Map<String, String> requestDto = Map.of(PROMO_CODE_KEYWORD_KEY, promoCodeKeyword);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().patch(URL_RIDES_ID_APPLY_PROMO_TEMPLATE, BOOKED_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getBookedRideResponseDto()
                .withPromoCode(promoCodeKeyword)
                .withFinalCost(RIDE_FINAL_COST_WITH_BRILLIANT10_PROMO_CODE)
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(result.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(result.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenChangePaymentMethod() {

        String paymentMethod = PaymentMethodEnum.CASH.toString();
        Map<String, String> requestDto = Map.of(PAYMENT_METHOD_KEY, paymentMethod);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, BOOKED_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getBookedRideResponseDto()
                .withPaymentMethod(paymentMethod)
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(result.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(result.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenScoreDriver() {

        Integer score = DEFAULT_SCORE;
        Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, score);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, FINISHED_NO_SCORES_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getFinishedNoScoresRideResponseDto()
                .withDriverScore(score)
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(result.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(result.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenScorePassenger() {

        Integer score = DEFAULT_SCORE;
        Map<String, Integer> requestDto = Map.of(PASSENGER_SCORE_KEY, score);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, FINISHED_NO_SCORES_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getFinishedNoScoresRideResponseDto()
                .withPassengerScore(score)
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(result.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(result.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Test
    void shouldReturn204AndMinusOneRide_whenDeleteRide() {

        List<Ride> ridesBeforeDelete = rideRepository.findAll();

        String rideId = FINISHED_NO_SCORES_RIDE_ID;
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete(URL_RIDES_ID_TEMPLATE, rideId);

        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        List<Ride> ridesAfterDelete = rideRepository.findAll();

        assertThat(ridesAfterDelete).size()
                .isEqualTo(ridesBeforeDelete.size() - 1);

        Predicate<? super Ride> matchById = p -> p.getId()
                .equals(UUID.fromString(rideId));
        assertThat(ridesBeforeDelete)
                .anyMatch(matchById);
        assertThat(ridesAfterDelete)
                .noneMatch(matchById);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetRideById() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_ID_TEMPLATE, FINISHED_NO_SCORES_RIDE_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto result = response.as(RideResponseDto.class);
        RideResponseDto expected = getFinishedNoScoresRideResponseDto().build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(result.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(result.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetRidesWithNoRequestParams() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<RideResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<RideResponseDto>>() {
                });
        ListContainerResponseDto<RideResponseDto> expected = getListContainerForResponse(RideResponseDto.class)
                .withValues(List.of(
                        getBookedRideResponseDto().build(),
                        getAcceptedRideResponseDto().build(),
                        getBeganBankCardRideResponseDto().build(),
                        getBeganCashRideResponseDto().build(),
                        getEndedCashNotPaidRideResponseDto().build(),
                        getEndedBankCardNotPaidRideResponseDto().build(),
                        getEndedBankCardPaidRideResponseDto().build(),
                        getFinishedNoScoresRideResponseDto().build(),
                        getFinishedWithScoresRideResponseDto().build()))
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(result.values())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(FIELDS_FOR_LIST_TO_IGNORE)
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetRidesWithPassengerIdRequestParam() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(PASSENGER_ID_REQUEST_PARAM, PASSENGER_4_ID)
                .when().get(URL_RIDES);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<RideResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<RideResponseDto>>() {
                });
        ListContainerResponseDto<RideResponseDto> expected = getListContainerForResponse(RideResponseDto.class)
                .withValues(List.of(
                        getEndedCashNotPaidRideResponseDto().build(),
                        getEndedBankCardNotPaidRideResponseDto().build(),
                        getEndedBankCardPaidRideResponseDto().build()))
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(result.values())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(FIELDS_FOR_LIST_TO_IGNORE)
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetRidesByPassengerId() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_PASSENGER_ID_TEMPLATE, PASSENGER_4_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<RideResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<RideResponseDto>>() {
                });
        ListContainerResponseDto<RideResponseDto> expected = getListContainerForResponse(RideResponseDto.class)
                .withValues(List.of(
                        getEndedCashNotPaidRideResponseDto().build(),
                        getEndedBankCardNotPaidRideResponseDto().build(),
                        getEndedBankCardPaidRideResponseDto().build()))
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(result.values())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(FIELDS_FOR_LIST_TO_IGNORE)
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetRidesByDriverId() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_DRIVER_ID_TEMPLATE, DRIVER_3_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<RideResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<RideResponseDto>>() {
                });
        ListContainerResponseDto<RideResponseDto> expected = getListContainerForResponse(RideResponseDto.class)
                .withValues(List.of(
                        getEndedCashNotPaidRideResponseDto().build(),
                        getEndedBankCardNotPaidRideResponseDto().build(),
                        getEndedBankCardPaidRideResponseDto().build()))
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(result.values())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(FIELDS_FOR_LIST_TO_IGNORE)
                .containsExactlyInAnyOrderElementsOf(expected.values());

    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPassengerRating() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_PASSENGER_ID_RATING_TEMPLATE, PASSENGER_1_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RatingResponseDto result = response.as(RatingResponseDto.class);
        RatingResponseDto expected = getPassengerRating().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetDriverRating() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_DRIVER_ID_RATING_TEMPLATE, DRIVER_4_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RatingResponseDto result = response.as(RatingResponseDto.class);
        RatingResponseDto expected = getDriverRating().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
