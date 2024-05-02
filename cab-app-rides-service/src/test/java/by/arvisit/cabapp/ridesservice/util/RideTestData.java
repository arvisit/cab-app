package by.arvisit.cabapp.ridesservice.util;

import static by.arvisit.cabapp.common.util.CommonConstants.EUROPE_MINSK_TIMEZONE;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentRequestDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RatingResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import by.arvisit.cabapp.ridesservice.persistence.model.RideStatusEnum;
import by.arvisit.cabapp.ridesservice.persistence.model.UserTypeEnum;

public final class RideTestData {

    private static final String PASSENGER_PAYMENT_DEFAULT_STATUS = "SUCCESS";
    private static final BigDecimal PASSENGER_PAYMENT_DEFAULT_FEE_AMOUNT = BigDecimal.valueOf(5);
    public static final String PASSENGER_PAYMENT_DEFAULT_ID = "cce748fb-1a3a-468e-a49e-08a26fe2a418";
    public static final int RIDE_DEFAULT_DRIVER_SCORE = 5;
    public static final int RIDE_DEFAULT_PASSENGER_SCORE = 4;
    public static final double DEFAULT_RATING = 4.0;
    public static final boolean RIDE_DEFAULT_IS_PAID = false;
    public static final boolean RIDE_FINISHED_IS_PAID = true;
    public static final ZonedDateTime RIDE_DEFAULT_BOOK_RIDE = ZonedDateTime.of(2024, 4, 4, 12, 0, 0, 0,
            EUROPE_MINSK_TIMEZONE);
    public static final ZonedDateTime RIDE_DEFAULT_CANCEL_RIDE = ZonedDateTime.of(2024, 4, 4, 12, 1, 0, 0,
            EUROPE_MINSK_TIMEZONE);
    public static final ZonedDateTime RIDE_DEFAULT_ACCEPT_RIDE = ZonedDateTime.of(2024, 4, 4, 12, 2, 0, 0,
            EUROPE_MINSK_TIMEZONE);
    public static final ZonedDateTime RIDE_DEFAULT_BEGIN_RIDE = ZonedDateTime.of(2024, 4, 4, 12, 3, 0, 0,
            EUROPE_MINSK_TIMEZONE);
    public static final ZonedDateTime RIDE_DEFAULT_END_RIDE = ZonedDateTime.of(2024, 4, 4, 12, 4, 0, 0,
            EUROPE_MINSK_TIMEZONE);
    public static final ZonedDateTime RIDE_DEFAULT_FINISH_RIDE = ZonedDateTime.of(2024, 4, 4, 12, 5, 0, 0,
            EUROPE_MINSK_TIMEZONE);
    public static final ZonedDateTime PASSENGER_PAYMENT_DEFAULT_TIMESTAMP = ZonedDateTime.of(2024, 4, 4, 12, 6, 0, 0,
            EUROPE_MINSK_TIMEZONE);
    public static final BigDecimal RIDE_DEFAULT_INITIAL_COST = BigDecimal.valueOf(100);
    public static final BigDecimal RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE = BigDecimal.valueOf(100);
    public static final BigDecimal RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE = BigDecimal.valueOf(90);
    public static final String RIDE_DEFAULT_ID_STRING = "a5b0a1f9-f45d-4287-bbad-ea6253976d0d";
    public static final UUID RIDE_DEFAULT_ID_UUID = UUID.fromString(RIDE_DEFAULT_ID_STRING);
    public static final String RIDE_DEFAULT_DESTINATION_ADDRESS = "Llewellyn Street, 35";
    public static final String RIDE_DEFAULT_START_ADDRESS = "Bramcote Grove, 42";
    public static final PaymentMethodEnum RIDE_DEFAULT_PAYMENT_METHOD_ENUM = PaymentMethodEnum.BANK_CARD;
    public static final String RIDE_DEFAULT_PAYMENT_METHOD_STRING = RIDE_DEFAULT_PAYMENT_METHOD_ENUM.toString();
    public static final String RIDE_DEFAULT_PASSENGER_ID_STRING = "3abcc6a1-94da-4185-aaa1-8a11c1b8efd2";
    public static final UUID RIDE_DEFAULT_PASSENGER_ID_UUID = UUID.fromString(RIDE_DEFAULT_PASSENGER_ID_STRING);
    public static final String RIDE_DEFAULT_DRIVER_ID_STRING = "a7dd0543-0adc-4ea6-9ca7-7b72065ca011";
    public static final UUID RIDE_DEFAULT_DRIVER_ID_UUID = UUID.fromString(RIDE_DEFAULT_DRIVER_ID_STRING);
    public static final boolean PROMO_CODE_DEFAULT_IS_ACTIVE = true;
    public static final long PROMO_CODE_DEFAULT_ID = 1L;
    public static final String PROMO_CODE_DEFAULT_KEYWORD = "PROMO";
    public static final int PROMO_CODE_DEFAULT_DISCOUNT_PERCENT = 10;
    public static final int PROMO_CODE_NEW_DISCOUNT_PERCENT = 20;
    public static final String PROMO_CODE_NEW_KEYWORD = "NEWPROMO";
    public static final int DEFAULT_PAGEABLE_SIZE = 10;
    public static final String UNSORTED = "UNSORTED";
    public static final String INVALID_PAYMENT_METHOD = "DIAMONDS";
    public static final String DEFAULT_PASSENGER_CARD_NUMBER = "7853471929691513";
    public static final String DEFAULT_PASSENGER_EMAIL = "vivienne.gutierrez@yahoo.com.ar";
    public static final String DEFAULT_PASSENGER_NAME = "Vivienne Gutierrez";

    public static final String URL_PROMO_CODES = "/api/v1/promo-codes";
    public static final String URL_PROMO_CODES_ACTIVE = "/api/v1/promo-codes/active";
    public static final String URL_PROMO_CODES_ID_TEMPLATE = "/api/v1/promo-codes/{id}";
    public static final String URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE = "/api/v1/promo-codes/{id}/deactivate";

    public static final String URL_RIDES = "/api/v1/rides";
    public static final String URL_RIDES_ID_TEMPLATE = "/api/v1/rides/{id}";
    public static final String URL_RIDES_ID_CANCEL_TEMPLATE = "/api/v1/rides/{id}/cancel";
    public static final String URL_RIDES_ID_ACCEPT_TEMPLATE = "/api/v1/rides/{id}/accept";
    public static final String URL_RIDES_ID_BEGIN_TEMPLATE = "/api/v1/rides/{id}/begin";
    public static final String URL_RIDES_ID_END_TEMPLATE = "/api/v1/rides/{id}/end";
    public static final String URL_RIDES_ID_FINISH_TEMPLATE = "/api/v1/rides/{id}/finish";
    public static final String URL_RIDES_ID_CONFIRM_PAYMENT_TEMPLATE = "/api/v1/rides/{id}/confirm-payment";
    public static final String URL_RIDES_ID_APPLY_PROMO_TEMPLATE = "/api/v1/rides/{id}/apply-promo";
    public static final String URL_RIDES_ID_CHANGE_PAYMENT_METHOD_TEMPLATE = "/api/v1/rides/{id}/change-payment-method";
    public static final String URL_RIDES_ID_SCORE_DRIVER_TEMPLATE = "/api/v1/rides/{id}/score-driver";
    public static final String URL_RIDES_ID_SCORE_PASSENGER_TEMPLATE = "/api/v1/rides/{id}/score-passenger";
    public static final String URL_RIDES_PASSENGER_ID_RATING_TEMPLATE = "/api/v1/rides/passengers/{id}/rating";
    public static final String URL_RIDES_DRIVER_ID_RATING_TEMPLATE = "/api/v1/rides/drivers/{id}/rating";
    public static final String URL_RIDES_PASSENGER_ID_TEMPLATE = "/api/v1/rides/passengers/{id}";
    public static final String URL_RIDES_DRIVER_ID_TEMPLATE = "/api/v1/rides/drivers/{id}";
    public static final String URL_RIDES_PARAM_VALUE_TEMPLATE = "/api/v1/rides?{param}={value}";

    private RideTestData() {
    }

    public static PromoCodeRequestDto.PromoCodeRequestDtoBuilder getPromoCodeRequestDto() {
        return PromoCodeRequestDto.builder()
                .withKeyword(PROMO_CODE_DEFAULT_KEYWORD)
                .withDiscountPercent(PROMO_CODE_DEFAULT_DISCOUNT_PERCENT);
    }

    public static PromoCode.PromoCodeBuilder getPromoCode() {
        return PromoCode.builder()
                .withId(PROMO_CODE_DEFAULT_ID)
                .withIsActive(PROMO_CODE_DEFAULT_IS_ACTIVE)
                .withKeyword(PROMO_CODE_DEFAULT_KEYWORD)
                .withDiscountPercent(PROMO_CODE_DEFAULT_DISCOUNT_PERCENT);
    }

    public static PromoCodeResponseDto.PromoCodeResponseDtoBuilder getPromoCodeResponseDto() {
        return PromoCodeResponseDto.builder()
                .withId(PROMO_CODE_DEFAULT_ID)
                .withIsActive(PROMO_CODE_DEFAULT_IS_ACTIVE)
                .withKeyword(PROMO_CODE_DEFAULT_KEYWORD)
                .withDiscountPercent(PROMO_CODE_DEFAULT_DISCOUNT_PERCENT);
    }

    public static RideRequestDto.RideRequestDtoBuilder getRideRequestDto() {
        return RideRequestDto.builder()
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS);
    }

    public static Ride.RideBuilder getBookedRide() {
        return Ride.builder()
                .withId(RIDE_DEFAULT_ID_UUID)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_UUID)
                .withDriverId(null)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_ENUM)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.BOOKED)
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(null)
                .withBeginRide(null)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getBookedRideResponseDto() {
        return RideResponseDto.builder()
                .withId(RIDE_DEFAULT_ID_STRING)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withDriverId(null)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.BOOKED.toString())
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(null)
                .withBeginRide(null)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static Ride.RideBuilder getBookedWithPromoCodeRide() {
        return Ride.builder()
                .withId(RIDE_DEFAULT_ID_UUID)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_UUID)
                .withDriverId(null)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_ENUM)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.BOOKED)
                .withPromoCode(getPromoCode().build())
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(null)
                .withBeginRide(null)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getBookedWithPromoCodeRideResponseDto() {
        return RideResponseDto.builder()
                .withId(RIDE_DEFAULT_ID_STRING)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withDriverId(null)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.BOOKED.toString())
                .withPromoCode(PROMO_CODE_DEFAULT_KEYWORD)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(null)
                .withBeginRide(null)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static Ride.RideBuilder getCanceledRide() {
        return Ride.builder()
                .withId(RIDE_DEFAULT_ID_UUID)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_UUID)
                .withDriverId(null)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_ENUM)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.CANCELED)
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(RIDE_DEFAULT_CANCEL_RIDE)
                .withAcceptRide(null)
                .withBeginRide(null)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getCanceledRideResponseDto() {
        return RideResponseDto.builder()
                .withId(RIDE_DEFAULT_ID_STRING)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withDriverId(null)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.CANCELED.toString())
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(RIDE_DEFAULT_CANCEL_RIDE)
                .withAcceptRide(null)
                .withBeginRide(null)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static Ride.RideBuilder getAcceptedRide() {
        return Ride.builder()
                .withId(RIDE_DEFAULT_ID_UUID)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_UUID)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_UUID)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_ENUM)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.ACCEPTED)
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(null)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getAcceptedRideResponseDto() {
        return RideResponseDto.builder()
                .withId(RIDE_DEFAULT_ID_STRING)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_STRING)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.ACCEPTED.toString())
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(null)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static Ride.RideBuilder getBeganRide() {
        return Ride.builder()
                .withId(RIDE_DEFAULT_ID_UUID)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_UUID)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_UUID)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_ENUM)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.BEGIN_RIDE)
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getBeganRideResponseDto() {
        return RideResponseDto.builder()
                .withId(RIDE_DEFAULT_ID_STRING)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_STRING)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.BEGIN_RIDE.toString())
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static Ride.RideBuilder getEndedRide() {
        return Ride.builder()
                .withId(RIDE_DEFAULT_ID_UUID)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_UUID)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_UUID)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_ENUM)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.END_RIDE)
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getEndedRideResponseDto() {
        return RideResponseDto.builder()
                .withId(RIDE_DEFAULT_ID_STRING)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_STRING)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_DEFAULT_IS_PAID)
                .withStatus(RideStatusEnum.END_RIDE.toString())
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(null);
    }

    public static Ride.RideBuilder getFinishedRide() {
        return Ride.builder()
                .withId(RIDE_DEFAULT_ID_UUID)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_UUID)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_UUID)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_ENUM)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_FINISHED_IS_PAID)
                .withStatus(RideStatusEnum.FINISHED)
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(RIDE_DEFAULT_FINISH_RIDE);
    }

    public static RideResponseDto.RideResponseDtoBuilder getFinishedRideResponseDto() {
        return RideResponseDto.builder()
                .withId(RIDE_DEFAULT_ID_STRING)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_STRING)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_FINISHED_IS_PAID)
                .withStatus(RideStatusEnum.FINISHED.toString())
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(RIDE_DEFAULT_FINISH_RIDE);
    }

    public static Ride.RideBuilder getFinishedWithScoresRide() {
        return Ride.builder()
                .withId(RIDE_DEFAULT_ID_UUID)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_UUID)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_UUID)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_ENUM)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_FINISHED_IS_PAID)
                .withStatus(RideStatusEnum.FINISHED)
                .withPromoCode(null)
                .withPassengerScore(RIDE_DEFAULT_PASSENGER_SCORE)
                .withDriverScore(RIDE_DEFAULT_DRIVER_SCORE)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(RIDE_DEFAULT_FINISH_RIDE);
    }

    public static RideResponseDto.RideResponseDtoBuilder getFinishedWithScoresRideResponseDto() {
        return RideResponseDto.builder()
                .withId(RIDE_DEFAULT_ID_STRING)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_STRING)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withIsPaid(RIDE_FINISHED_IS_PAID)
                .withStatus(RideStatusEnum.FINISHED.toString())
                .withPromoCode(null)
                .withPassengerScore(RIDE_DEFAULT_PASSENGER_SCORE)
                .withDriverScore(RIDE_DEFAULT_DRIVER_SCORE)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(RIDE_DEFAULT_FINISH_RIDE);
    }

    public static Ride.RideBuilder getFinishedWithScoresAndPromoCodeRide() {
        return Ride.builder()
                .withId(RIDE_DEFAULT_ID_UUID)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_UUID)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_UUID)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_ENUM)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE)
                .withIsPaid(RIDE_FINISHED_IS_PAID)
                .withStatus(RideStatusEnum.FINISHED)
                .withPromoCode(getPromoCode().build())
                .withPassengerScore(RIDE_DEFAULT_PASSENGER_SCORE)
                .withDriverScore(RIDE_DEFAULT_DRIVER_SCORE)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(RIDE_DEFAULT_FINISH_RIDE);
    }

    public static RideResponseDto.RideResponseDtoBuilder getFinishedWithScoresAndPromoCodeRideResponseDto() {
        return RideResponseDto.builder()
                .withId(RIDE_DEFAULT_ID_STRING)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_STRING)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE)
                .withIsPaid(RIDE_FINISHED_IS_PAID)
                .withStatus(RideStatusEnum.FINISHED.toString())
                .withPromoCode(PROMO_CODE_DEFAULT_KEYWORD)
                .withPassengerScore(RIDE_DEFAULT_PASSENGER_SCORE)
                .withDriverScore(RIDE_DEFAULT_DRIVER_SCORE)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(RIDE_DEFAULT_FINISH_RIDE);
    }

    public static PassengerPaymentRequestDto.PassengerPaymentRequestDtoBuilder getPassengerPaymentRequestDto() {
        return PassengerPaymentRequestDto.builder()
                .withRideId(RIDE_DEFAULT_ID_STRING)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_STRING)
                .withAmount(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withCardNumber(DEFAULT_PASSENGER_CARD_NUMBER);
    }
    
    public static PassengerPaymentResponseDto.PassengerPaymentResponseDtoBuilder getPassengerPaymentResponseDto() {
        return PassengerPaymentResponseDto.builder()
                .withId(PASSENGER_PAYMENT_DEFAULT_ID)
                .withRideId(RIDE_DEFAULT_ID_STRING)
                .withPassengerId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withDriverId(RIDE_DEFAULT_DRIVER_ID_STRING)
                .withAmount(RIDE_DEFAULT_FINAL_COST_NO_PROMO_CODE)
                .withFeeAmount(PASSENGER_PAYMENT_DEFAULT_FEE_AMOUNT)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withCardNumber(DEFAULT_PASSENGER_CARD_NUMBER)
                .withStatus(PASSENGER_PAYMENT_DEFAULT_STATUS)
                .withTimestamp(PASSENGER_PAYMENT_DEFAULT_TIMESTAMP);
    }

    public static RatingResponseDto.RatingResponseDtoBuilder getPassengerRating() {
        return RatingResponseDto.builder()
                .withUserType(UserTypeEnum.PASSENGER)
                .withUserId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withRating(DEFAULT_RATING);
    }

    public static RatingResponseDto.RatingResponseDtoBuilder getDriverRating() {
        return RatingResponseDto.builder()
                .withUserType(UserTypeEnum.DRIVER)
                .withUserId(RIDE_DEFAULT_DRIVER_ID_STRING)
                .withRating(DEFAULT_RATING);
    }

    public static PassengerResponseDto.PassengerResponseDtoBuilder getPassengerResponseDto() {
        return PassengerResponseDto.builder()
                .withId(RIDE_DEFAULT_PASSENGER_ID_STRING)
                .withName(DEFAULT_PASSENGER_NAME)
                .withEmail(DEFAULT_PASSENGER_EMAIL)
                .withCardNumber(DEFAULT_PASSENGER_CARD_NUMBER);
    }

    public static ListContainerResponseDto.ListContainerResponseDtoBuilder<PromoCodeResponseDto> getPromoCodeResponseDtoInListContainer() {
        return ListContainerResponseDto.<PromoCodeResponseDto>builder()
                .withValues(List.of(getPromoCodeResponseDto().build()))
                .withCurrentPage(0)
                .withSize(DEFAULT_PAGEABLE_SIZE)
                .withLastPage(0)
                .withSort(UNSORTED);
    }

    public static ListContainerResponseDto.ListContainerResponseDtoBuilder<RideResponseDto> getRideResponseDtoInListContainer() {
        return ListContainerResponseDto.<RideResponseDto>builder()
                .withValues(List.of(getFinishedRideResponseDto().build()))
                .withCurrentPage(0)
                .withSize(DEFAULT_PAGEABLE_SIZE)
                .withLastPage(0)
                .withSort(UNSORTED);
    }
}
