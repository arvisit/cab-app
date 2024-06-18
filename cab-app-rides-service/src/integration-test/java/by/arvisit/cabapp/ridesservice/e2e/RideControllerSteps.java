package by.arvisit.cabapp.ridesservice.e2e;

import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.BOOKED_RIDE_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DIAMOND10_KEYWORD;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DRIVER_1_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DRIVER_ID_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.DRIVER_SCORE_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.PASSENGER_SCORE_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.PAYMENT_METHOD_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.PROMO_CODE_KEYWORD_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.RIDE_FINAL_COST_WITH_DIAMOND10_PROMO_CODE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_DRIVER_ID_RATING_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_ACCEPT_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_APPLY_PROMO_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_BEGIN_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_CANCEL_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_END_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_SCORE_DRIVER_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_RIDES_PASSENGER_ID_RATING_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getAcceptedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getAddedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getBeganBankCardRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getBookedRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getDriverRating;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getEndedCashNotPaidRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getFinishedWithScoresRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getNewRideRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getPassengerRating;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RatingResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.ridesservice.persistence.model.RideStatusEnum;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RideControllerSteps {

    private static final String PAGE_REQUEST_PARAM = "page";
    private static final String RIDE_ID_QUERY_PARAM = "rideId";
    private static final String URL_DRIVERS_ID_TEMPLATE = "/api/v1/drivers/{id}";
    private static final String URL_PASSENGER_PAYMENTS = "/api/v1/passenger-payments";
    private static final int DRIVER_SERVICE_PORT = Integer.parseInt(System.getProperty("driverServerPort"));
    private static final int PAYMENT_SERVICE_PORT = Integer.parseInt(System.getProperty("paymentServerPort"));
    private static final String[] TIMESTAMP_FIELDS = { "bookRide", "cancelRide", "acceptRide", "beginRide", "endRide",
            "finishRide" };
    private static final String[] BIG_DECIMAL_FIELDS = { "initialCost", "finalCost" };

    private RideRequestDto rideRequest;
    private Response response;
    private List<RideResponseDto> ridesBeforeDelete;

    @Given("User wants to save a new ride with passenger id {string}, payment method {string}, start address {string} and destination address {string}")
    public void prepareNewRideToSave(String passengerId, String paymentMethod, String startAddress,
            String destinationAddress) {

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

    @Then("after {int} seconds this ride should be accepted by driver")
    public void checkRideForDefaultAcceptance(int timeout) throws Exception {
        RideResponseDto newRide = response.as(RideResponseDto.class);

        assertThat(newRide.driverId())
                .isNull();

        String rideId = newRide.id();

        TimeUnit.SECONDS.sleep(timeout);

        Response tmpResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_ID_TEMPLATE, rideId);

        RideResponseDto actual = tmpResponse.as(RideResponseDto.class);

        assertThat(actual.driverId())
                .isNotNull();

        cancelRide(rideId);
    }

    @Given("User wants to cancel an accepted ride")
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
        RideResponseDto expected = getAcceptedRideResponseDto()
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

    @Then("assigned driver should become available")
    public void checkDriverAvailabilityFromRideResponse() throws Exception {
        RideResponseDto ride = response.as(RideResponseDto.class);
        String driverId = ride.driverId();

        TimeUnit.SECONDS.sleep(1);

        Response tmpResponse = RestAssured.given()
                .port(DRIVER_SERVICE_PORT)
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVERS_ID_TEMPLATE, driverId);
        DriverResponseDto actual = tmpResponse.as(DriverResponseDto.class);

        assertThat(actual.isAvailable())
                .isTrue();
    }

    @Given("User wants to accept ride by existing driver")
    public void prepareToAcceptRide() throws Exception {
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

    @Then("driver with id {string} should become unavailable")
    public void checkDriverAvailabilityAfterAcceptRide(String driverId) throws Exception {
        TimeUnit.SECONDS.sleep(1);

        Response tmpResponse = RestAssured.given()
                .port(DRIVER_SERVICE_PORT)
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVERS_ID_TEMPLATE, driverId);
        DriverResponseDto actual = tmpResponse.as(DriverResponseDto.class);

        assertThat(actual.isAvailable())
                .isFalse();
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

        assertThat(actual.beginRide())
                .isNotNull();
        assertThat(actual.status())
                .isEqualTo(RideStatusEnum.BEGIN_RIDE.toString());
    }

    @Given("User wants to end a began ride with bank card payment method")
    public void prepareToEndRideWithBankCardPaymentMethod() throws Exception {
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

    @Then("ride should become finished eventually")
    public void checkEndedRideToBecomeFinished() throws Exception {
        TimeUnit.SECONDS.sleep(1);

        RideResponseDto ride = response.as(RideResponseDto.class);
        Response tmpResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_ID_TEMPLATE, ride.id());

        RideResponseDto actual = tmpResponse.as(RideResponseDto.class);

        assertThat(actual.status())
                .isEqualTo(RideStatusEnum.FINISHED.toString());
    }

    @Then("payment for ride should be created")
    public void checkPaymentAfterEndRideWithBankCard() {
        RideResponseDto ride = response.as(RideResponseDto.class);
        Response tmpResponse = RestAssured.given()
                .port(PAYMENT_SERVICE_PORT)
                .contentType(ContentType.JSON)
                .queryParam(RIDE_ID_QUERY_PARAM, ride.id())
                .when().get(URL_PASSENGER_PAYMENTS);

        ListContainerResponseDto<PassengerPaymentResponseDto> actual = tmpResponse
                .as(new TypeRef<ListContainerResponseDto<PassengerPaymentResponseDto>>() {
                });

        assertThat(actual.values())
                .hasSize(1);
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

        assertThat(actual.endRide())
                .isNotNull();
        assertThat(actual.status())
                .isEqualTo(RideStatusEnum.END_RIDE.toString());
    }

    @Then("ride would not become finished after timeout {int} seconds")
    public void checkEndRideWithCashPaymentMethodResponse(int timeout) throws Exception {
        TimeUnit.SECONDS.sleep(timeout);

        RideResponseDto ride = response.as(RideResponseDto.class);
        Response tmpResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_RIDES_ID_TEMPLATE, ride.id());

        RideResponseDto actual = tmpResponse.as(RideResponseDto.class);

        assertThat(actual.status())
                .isEqualTo(RideStatusEnum.END_RIDE.toString());
    }

    @Given("User wants to confirm payment with cash for an existing ride")
    public void prepareConfirmPaymentCashRequest() throws Exception {
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

        assertThat(actual.promoCode())
                .isEqualTo(DIAMOND10_KEYWORD);
        assertThat(actual.finalCost())
                .isEqualByComparingTo(RIDE_FINAL_COST_WITH_DIAMOND10_PROMO_CODE);
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

        assertThat(actual.paymentMethod())
                .isEqualTo(PaymentMethodEnum.CASH.toString());
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

    @Then("response should have 200 status, json content type, contain ride with driver score {int}")
    public void checkScoreDriverResponse(int driverScore) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);

        assertThat(actual.driverScore())
                .isEqualTo(driverScore);
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

    @Then("response should have 200 status, json content type, contain ride with passenger score {int}")
    public void checkScorePassengerResponse(int passengerScore) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        RideResponseDto actual = response.as(RideResponseDto.class);

        assertThat(actual.passengerScore())
                .isEqualTo(passengerScore);
    }

    @Given("User wants to delete an existing ride")
    public void prepareDeleteRideRequest() {
        ridesBeforeDelete = extractAllItems();
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

        List<RideResponseDto> ridesAfterDelete = extractAllItems();

        assertThat(ridesAfterDelete).size()
                .isEqualTo(ridesBeforeDelete.size() - 1);

        Predicate<? super RideResponseDto> matchById = p -> p.id()
                .equals(id);
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
        RideResponseDto expected = getFinishedWithScoresRideResponseDto().build();

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

    private void cancelRide(String id) {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_RIDES_ID_CANCEL_TEMPLATE, id);
    }

    private List<RideResponseDto> extractAllItems() {
        List<RideResponseDto> items = new ArrayList<>();

        int nextPage = 0;
        int lastPage = 0;

        do {
            Response tmpResponse = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .queryParam(PAGE_REQUEST_PARAM, nextPage)
                    .when().get(URL_RIDES);
            ListContainerResponseDto<RideResponseDto> itemsContainer = tmpResponse
                    .as(new TypeRef<ListContainerResponseDto<RideResponseDto>>() {
                    });
            items.addAll(itemsContainer.values());
            lastPage = itemsContainer.lastPage();
            nextPage++;
        } while (nextPage <= lastPage);

        return items;
    }
}
