package by.arvisit.cabapp.ridesservice.component;

import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.BOOKED_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.BRILLIANT10_KEYWORD;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DEFAULT_SCORE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DRIVER_1_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DRIVER_ID_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DRIVER_SCORE_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.PASSENGER_3_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.PASSENGER_4_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.PASSENGER_SCORE_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.PAYMENT_METHOD_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.PROMO_CODE_KEYWORD_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.RIDE_FINAL_COST_WITH_BRILLIANT10_PROMO_CODE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_DRIVER_ID_RATING_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_DRIVER_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_ACCEPT_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_APPLY_PROMO_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_BEGIN_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_CANCEL_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_END_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_FINISH_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_SCORE_DRIVER_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_PASSENGER_ID_RATING_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_PASSENGER_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getAcceptedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getAddedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getBeganBankCardRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getBeganCashRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getBookedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getDriverRating;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getDriverResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getEndedBankCardNotPaidRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getEndedBankCardPaidRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getEndedCashNotPaidRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getFinishedNoScoresRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getFinishedWithScoresRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getListContainerForResponse;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getNewRideRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getPassengerPaymentResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getPassengerRating;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getPassengerResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RatingResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.mock.WireMockService;
import by.arvisit.cabapp.ridesservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import by.arvisit.cabapp.ridesservice.persistence.model.RideStatusEnum;
import by.arvisit.cabapp.ridesservice.persistence.repository.RideRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RideControllerSteps {

    private static final String PASSENGER_ID_REQUEST_PARAM = "passengerId";
    private static final String[] TIMESTAMP_FIELDS = { "bookRide", "cancelRide", "acceptRide", "beginRide", "endRide",
            "finishRide" };
    private static final String[] BIG_DECIMAL_FIELDS = { "initialCost", "finalCost" };
    private static final String[] FIELDS_FOR_LIST_TO_IGNORE = { "initialCost", "finalCost", "bookRide", "cancelRide",
            "acceptRide", "beginRide", "endRide", "finishRide" };
    private static final String VALUES_FIELD = "values";

    @Autowired
    private WireMockService wireMockService;
    @Autowired
    private RideRepository rideRepository;

    private RideRequestDto rideRequest;
    private Response response;
    private List<Ride> ridesBeforeDelete;

    @Given("User wants to save a new ride with passenger id {string}, payment method {string}, start address {string} and destination address {string}")
    public void prepareNewRideToSave(String passengerId, String paymentMethod, String startAddress,
            String destinationAddress) throws Exception {
        PassengerResponseDto passenger = getPassengerResponseDto().build();
        wireMockService.mockResponseForPassengerClientGetPassengerById(passenger);

        rideRequest = getNewRideRequestDto()
                .withPassengerId(passengerId)
                .withPaymentMethod(paymentMethod)
                .withStartAddress(startAddress)
                .withDestinationAddress(destinationAddress)
                .build();
    }

    @When("he performs saving of a new ride via request")
    public void sendSaveNewRideRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(rideRequest)
                .when().post(URL_RIDES);
    }

    @Then("response should have 201 status, json content type, contain ride with expected parameters and id")
    public void checkSaveNewRideResponse() {
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getAddedRideResponseDto().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "initialCost", "finalCost", "bookRide")
                .isEqualTo(expected);
        assertThat(actual.id())
                .isNotNull();
        assertThat(actual.initialCost())
                .isNotNull();
        assertThat(actual.finalCost())
                .isNotNull();
        assertThat(actual.bookRide())
                .isNotNull();
    }

    @Given("User wants to cancel an existing ride")
    public void prepareCancelRequest() {
    }

    @When("he performs a request to cancel ride with id {string}")
    public void sendCancelRideRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_RIDES_ID_CANCEL_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain canceled ride")
    public void checkCancelRideResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getBookedRideResponseDto()
                .withStatus(RideStatusEnum.CANCELED.toString())
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.cancelRide())
                .isNotNull();
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to accept ride by existing driver")
    public void prepareToAcceptRide() throws Exception {
        DriverResponseDto driverBeforeAccept = getDriverResponseDto().build();
        wireMockService.mockResponseForDriverClientGetDriverById(driverBeforeAccept);
        DriverResponseDto driverAfterAccept = getDriverResponseDto()
                .withIsAvailable(false)
                .build();
        wireMockService.mockResponseForDriverClientUpdateAvailability(driverAfterAccept);
    }

    @When("he performs a request to accept ride with id {string} by available driver with id {string}")
    public void sendAcceptRideRequest(String rideId, String driverId) {
        Map<String, String> requestDto = Map.of(DRIVER_ID_KEY, driverId);

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().patch(URL_RIDES_ID_ACCEPT_TEMPLATE, BOOKED_RIDE_ID);
    }

    @Then("response should have 200 status, json content type, contain accepted ride")
    public void checkAcceptRideResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getBookedRideResponseDto()
                .withStatus(RideStatusEnum.ACCEPTED.toString())
                .withDriverId(DRIVER_1_ID)
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.acceptRide())
                .isNotNull();
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to begin an accepted ride")
    public void prepareToBeginRide() {
    }

    @When("he performs a request to begin ride with id {string}")
    public void sendBeginRideRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_RIDES_ID_BEGIN_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain began ride")
    public void checkBeginRideResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getAcceptedRideResponseDto()
                .withStatus(RideStatusEnum.BEGIN_RIDE.toString())
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.beginRide())
                .isNotNull();
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to end a began ride with bank card payment method")
    public void prepareToEndRideWithBankCardPaymentMethod() throws Exception {
        PassengerResponseDto passenger = getPassengerResponseDto()
                .withId(PASSENGER_3_ID)
                .build();
        wireMockService.mockResponseForPassengerClientGetPassengerById(passenger);
    }

    @When("he performs a request to end ride with id {string}")
    public void sendEndRideRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_RIDES_ID_END_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain ended ride with bank card payment method")
    public void checkEndRideWithBankCardPaymentMethodResponse() throws Exception {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getBeganBankCardRideResponseDto()
                .withStatus(RideStatusEnum.END_RIDE.toString())
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.endRide())
                .isNotNull();
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to end a began ride with cash payment method")
    public void prepareEndRideWithCashPaymentMethodRequest() {
    }

    @Then("response should have 200 status, json content type, contain ended ride with cash payment method")
    public void checkEndRideWithCashPaymentMethodResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getBeganCashRideResponseDto()
                .withStatus(RideStatusEnum.END_RIDE.toString())
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.endRide())
                .isNotNull();
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to finish an ended ride")
    public void prepareFinishRideRequest() {
    }

    @When("he performs a request to finish ride with id {string}")
    public void sendFinishRideRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_RIDES_ID_FINISH_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain finished ride")
    public void checkFinishRideResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getEndedBankCardPaidRideResponseDto()
                .withStatus(RideStatusEnum.FINISHED.toString())
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.finishRide())
                .isNotNull();
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to confirm payment with cash for an existing ride")
    public void prepareConfirmPaymentCashRequest() throws Exception {
        PassengerPaymentResponseDto payment = getPassengerPaymentResponseDto().build();
        wireMockService.mockResponseForPaymentClientSave(payment);
    }

    @When("he performs a request to confirm payment for the ride with id {string}")
    public void sendConfirmPaymentRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain finished ride with cash payment method")
    public void checkConfirmPaymentCashResponse() throws Exception {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getEndedCashNotPaidRideResponseDto()
                .withStatus(RideStatusEnum.FINISHED.toString())
                .withIsPaid(true)
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.finishRide())
                .isNotNull();
        assertThat(actual.isPaid())
                .isTrue();
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to confirm payment with bank card for an existing ride")
    public void prepareConfirmPaymentBankCardRequest() {
    }

    @Then("response should have 200 status, json content type, contain finished ride with bank card payment method")
    public void checkConfirmPaymentBankCardResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getEndedBankCardNotPaidRideResponseDto()
                .withStatus(RideStatusEnum.FINISHED.toString())
                .withIsPaid(true)
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.finishRide())
                .isNotNull();
        assertThat(actual.isPaid())
                .isTrue();
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to apply promo code to an existing ride")
    public void prepareApplyPromoCodeRequest() {
    }

    @When("he performs a request to apply promo code {string} to the ride with id {string}")
    public void sendApplyPromoCodeRequest(String promoCodeKeyword, String rideId) {
        Map<String, String> requestDto = Map.of(PROMO_CODE_KEYWORD_KEY, promoCodeKeyword);

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().patch(URL_RIDES_ID_APPLY_PROMO_TEMPLATE, BOOKED_RIDE_ID);
    }

    @Then("response should have 200 status, json content type, contain ride with applied promo code")
    public void checkApplyPromoCodeResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getBookedRideResponseDto()
                .withPromoCode(BRILLIANT10_KEYWORD)
                .withFinalCost(RIDE_FINAL_COST_WITH_BRILLIANT10_PROMO_CODE)
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to change payment method for an existing ride")
    public void prepareChangePaymentMethodRequest() {
    }

    @When("he performs a request to change payment method for the ride with id {string}")
    public void sendChangePaymentMethodRequest(String id) {
        String paymentMethod = PaymentMethodEnum.CASH.toString();
        Map<String, String> requestDto = Map.of(PAYMENT_METHOD_KEY, paymentMethod);

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().patch(URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain ride with changed payment method")
    public void checkChangePaymentMethodResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getBookedRideResponseDto()
                .withPaymentMethod(PaymentMethodEnum.CASH.toString())
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to score a driver for an existing ride")
    public void prepareScoreDriverRequest() {
    }

    @When("he performs a request to score a driver with {int} for the ride with id {string}")
    public void sendScoreDriverRequest(int score, String rideId) {
        Map<String, Integer> requestDto = Map.of(DRIVER_SCORE_KEY, score);

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().patch(URL_RIDES_ID_SCORE_DRIVER_TEMPLATE, rideId);
    }

    @Then("response should have 200 status, json content type, contain ride with driver score")
    public void checkScoreDriverResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getFinishedNoScoresRideResponseDto()
                .withDriverScore(DEFAULT_SCORE)
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to score a passenger for an existing ride")
    public void prepareScorePassengerRequest() {
    }

    @When("he performs a request to score a passenger with {int} for the ride with id {string}")
    public void sendScorePassengerRequest(int score, String rideId) {
        Map<String, Integer> requestDto = Map.of(PASSENGER_SCORE_KEY, score);

        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().patch(URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE, rideId);

    }

    @Then("response should have 200 status, json content type, contain ride with passenger score")
    public void checkScorePassengerResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getFinishedNoScoresRideResponseDto()
                .withPassengerScore(DEFAULT_SCORE)
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to delete an existing ride")
    public void prepareDeleteRideRequest() {
        ridesBeforeDelete = rideRepository.findAll();
    }

    @When("he performs a request to delete a ride with id {string}")
    public void sendDeleteRideRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete(URL_RIDES_ID_TEMPLATE, id);
    }

    @Then("response should have 204 status, minus ride with id {string} in the database")
    public void checkDeleteRideResponse(String id) {
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        List<Ride> ridesAfterDelete = rideRepository.findAll();

        assertThat(ridesAfterDelete).size()
                .isEqualTo(ridesBeforeDelete.size() - 1);

        Predicate<? super Ride> matchById = p -> p.getId()
                .equals(UUID.fromString(id));
        assertThat(ridesBeforeDelete)
                .anyMatch(matchById);
        assertThat(ridesAfterDelete)
                .noneMatch(matchById);
    }

    @Given("User wants to get details about an existing ride")
    public void prepareGetRideByIdRequest() {
    }

    @When("he performs a request to get details about ride with id {string}")
    public void sendGetRideByIdRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_ID_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain ride with requested id")
    public void checkGetRideByIdResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);
        RideResponseDto expected = getFinishedNoScoresRideResponseDto().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELDS)
                .isEqualTo(expected);
        assertThat(actual.initialCost())
                .isEqualByComparingTo(expected.initialCost());
        assertThat(actual.finalCost())
                .isEqualByComparingTo(expected.finalCost());
    }

    @Given("User wants to get details about existing rides")
    public void prepareGeRidesWithNoRequestParamsRequest() {
    }

    @When("he performs a request with no request parameters to get all rides")
    public void sendGetRidesWithNoRequestParamsRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES);
    }

    @Then("response should have 200 status, json content type, contain info about {int} rides")
    public void checkGetRidesWithNoRequestParamsResponse(int ridesCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<RideResponseDto> actual = response
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

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(actual.values())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(FIELDS_FOR_LIST_TO_IGNORE)
                .containsExactlyInAnyOrderElementsOf(expected.values())
                .hasSize(ridesCount);
    }

    @Given("User wants to get details about existing rides for specific passenger")
    public void prepareGetRidesByPassengerIdRequest() {
    }

    @When("he performs a request with request parameter {string}={string} to get all rides for this passenger")
    public void sendGetRidesByPassengerIdAsRequestParamRequest(String passengerIdParam, String passengerId) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(PASSENGER_ID_REQUEST_PARAM, PASSENGER_4_ID)
                .when().get(URL_RIDES);
    }

    @Then("response should have 200 status, json content type, contain info about {int} rides for this passenger")
    public void checkGetRidesByPassengerIdResponse(int ridesCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<RideResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<RideResponseDto>>() {
                });
        ListContainerResponseDto<RideResponseDto> expected = getListContainerForResponse(RideResponseDto.class)
                .withValues(List.of(
                        getEndedCashNotPaidRideResponseDto().build(),
                        getEndedBankCardNotPaidRideResponseDto().build(),
                        getEndedBankCardPaidRideResponseDto().build()))
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(actual.values())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(FIELDS_FOR_LIST_TO_IGNORE)
                .containsExactlyInAnyOrderElementsOf(expected.values())
                .hasSize(ridesCount);
    }

    @When("he performs a request with passenger id {string} to get all rides for this passenger")
    public void sendGetRidesByPassengerIdAsPathParamRequest(String passengerId) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_PASSENGER_ID_TEMPLATE, passengerId);
    }

    @Given("User wants to get details about existing rides for specific driver")
    public void prepareGetRidesByDriverIdAsPathParam() {
    }

    @When("he performs a request with driver id {string} to get all rides for this driver")
    public void sendGetRidesByDriverIdAsPathParamRequest(String driverId) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_DRIVER_ID_TEMPLATE, driverId);
    }

    @Then("response should have 200 status, json content type, contain info about {int} rides for this driver")
    public void checkGetRidesByDriverIdResponse(int ridesCount) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<RideResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<RideResponseDto>>() {
                });
        ListContainerResponseDto<RideResponseDto> expected = getListContainerForResponse(RideResponseDto.class)
                .withValues(List.of(
                        getEndedCashNotPaidRideResponseDto().build(),
                        getEndedBankCardNotPaidRideResponseDto().build(),
                        getEndedBankCardPaidRideResponseDto().build()))
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(actual.values())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(FIELDS_FOR_LIST_TO_IGNORE)
                .containsExactlyInAnyOrderElementsOf(expected.values())
                .hasSize(ridesCount);
    }

    @Given("User wants to get rating for an existing passenger")
    public void prepareGetPassengerRatingRequest() {
    }

    @When("he performs a request with passenger id {string} to get his rating")
    public void sendGetPassengerRatingRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_PASSENGER_ID_RATING_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain rating for the passenger")
    public void checkGetPassengerRatingResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RatingResponseDto actual = response.as(RatingResponseDto.class);
        RatingResponseDto expected = getPassengerRating().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to get rating for an existing driver")
    public void prepareGetDriverRatingRequest() {
    }

    @When("he performs a request with driver id {string} to get his rating")
    public void sendGetDriverRatingRequest(String id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_DRIVER_ID_RATING_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain rating for the driver")
    public void checkGetDriverRatingResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RatingResponseDto actual = response.as(RatingResponseDto.class);
        RatingResponseDto expected = getDriverRating().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
