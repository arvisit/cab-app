package by.arvisit.cabapp.ridesservice.util;

import static by.arvisit.cabapp.common.util.CommonConstants.EUROPE_MINSK_TIMEZONE;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RatingResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.ridesservice.persistence.model.RideStatusEnum;
import by.arvisit.cabapp.ridesservice.persistence.model.UserTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RideIntegrationTestData {

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

    public static final boolean NEW_PROMO_CODE_IS_ACTIVE = true;
    public static final long NEW_PROMO_CODE_ID = 1L;
    public static final String NEW_PROMO_CODE_KEYWORD = "PROMO";
    public static final int NEW_PROMO_CODE_DISCOUNT_PERCENT = 20;
    public static final int RICE23_DISCOUNT_PERCENT = 23;
    public static final String RICE23_KEYWORD = "RICE23";
    public static final boolean RICE23_IS_ACTIVE = false;
    public static final long RICE23_ID = 4L;
    public static final int PAIN49_NOT_ACTIVE_PERCENT_DISCOUNT = 49;
    public static final boolean PAIN49_NOT_ACTIVE_IS_ACTIVE = false;
    public static final long PAIN49_NOT_ACTIVE_ID = 2L;
    public static final int PAIN49_ACTIVE_DISCOUNT_PERCENT = 49;
    public static final String PAIN49_ACTIVE_KEYWORD = "PAIN49";
    public static final String PAIN49_NOT_ACTIVE_KEYWORD = "PAIN49";
    public static final boolean PAIN49_ACTIVE_IS_ACTIVE = true;
    public static final long PAIN49_ACTIVE_ID = 1L;
    public static final int BRILLIANT10_DISCOUNT_PERCENT = 10;
    public static final String BRILLIANT10_KEYWORD = "BRILLIANT10";
    public static final boolean BRILLIANT10_IS_ACTIVE = true;
    public static final long BRILLIANT10_ID = 3L;
    public static final String DIAMOND10_KEYWORD = "DIAMOND10";
    public static final boolean DIAMOND10_IS_ACTIVE = true;
    public static final long DIAMON10_ID = 5L;

    public static final String ENDED_BANK_CARD_NOT_PAID_RIDE_ID = "942d8839-7a16-4dfa-9443-179220135b3a";
    public static final String PAYMENT_DEFAULT_STATUS = "SUCCESS";
    public static final String PASSENGER_PAYMENT_DEFAULT_ID = "cce748fb-1a3a-468e-a49e-08a26fe2a418";
    public static final String DEFAULT_PASSENGER_CARD_NUMBER = "7853471929691513";
    public static final String DEFAULT_PASSENGER_EMAIL = "vivienne.gutierrez@yahoo.com.ar";
    public static final String DEFAULT_PASSENGER_NAME = "Vivienne Gutierrez";

    public static final String DEFAULT_DRIVER_CARD_NUMBER = "1522613953683617";
    public static final String DEFAULT_DRIVER_EMAIL = "jeremias.olsen@frontiernet.net";
    public static final String DEFAULT_DRIVER_NAME = "Jeremias Olsen";
    public static final boolean DEFAULT_DRIVER_IS_AVAILABLE = true;

    public static final int DEFAULT_SCORE = 5;
    public static final double PASSENGER_1_RATING = 4.0;
    public static final double DRIVER_4_RATING = 5.0;
    public static final String RIDE_DEFAULT_DESTINATION_ADDRESS = "Llewellyn Street, 35";
    public static final String RIDE_DEFAULT_START_ADDRESS = "Bramcote Grove, 42";
    public static final String DRIVER_2_ID = "e90dbf58-e20d-458a-9a6d-68ac2ede9c37";
    public static final String DRIVER_3_ID = "d9343856-ad27-4256-9534-4c59fa5e6422";
    public static final String DRIVER_1_ID = "4c2b3a93-1d97-4ccf-a7b8-824daea08671";
    public static final String DRIVER_4_ID = "19ee7917-8e48-4b3e-8b21-28c3d1e53ca4";
    public static final String PASSENGER_4_ID = "deecaeef-454b-487d-987c-54df212385b3";
    public static final String PASSENGER_3_ID = "2b6716c3-0c8d-4a3d-90f5-49ebcc2b77d8";
    public static final String PASSENGER_2_ID = "3c4e9fdf-f9c1-4a59-8bc0-c3758151f7e0";
    public static final String PASSENGER_1_ID = "072f635e-0ee7-461e-aa7e-1901ae3d0c5e";
    public static final String PASSENGER_5_ID = "3abcc6a1-94da-4185-aaa1-8a11c1b8efd2";
    public static final String BOOKED_RIDE_ID = "69b509c6-20fe-4b70-a07e-31e52ea05a54";
    public static final String ACCEPTED_RIDE_ID = "a5b0a1f9-f45d-4287-bbad-ea6253976d0d";
    public static final String BEGAN_BANK_CARD_RIDE_ID = "72b20027-23d1-4651-865a-a2f7cd74fdb6";
    public static final String BEGAN_CASH_RIDE_ID = "acd6f53f-a19b-4109-9b6f-1d15814db2f1";
    public static final String ENDED_CASH_NOT_PAID_RIDE_ID = "ffe34487-dfa3-4660-96dc-ed108e06ab77";
    public static final String ENDED_BANK_CARD_PAID_RIDE_ID = "b4de939f-3a3f-441c-b081-dc604f6bed20";
    public static final String FINISHED_NO_SCORES_RIDE_ID = "9ea8a8b0-004b-4ded-8a3a-edc198c89e31";
    public static final String FINISHED_WITH_SCORES_RIDE_ID = "aba3e655-a8b1-4dca-a4b5-6566dd74a47e";
    public static final BigDecimal AGGREGATOR_FEE_PERCENT = BigDecimal.valueOf(5);
    public static final BigDecimal BIG_DECIMAL_HUNDRED = BigDecimal.valueOf(100);
    public static final BigDecimal RIDE_DEFAULT_FINAL_COST = BigDecimal.valueOf(100);
    public static final BigDecimal RIDE_DEFAULT_INITIAL_COST = BigDecimal.valueOf(100);
    public static final BigDecimal RIDE_FINAL_COST_WITH_BRILLIANT10_PROMO_CODE = RIDE_DEFAULT_FINAL_COST
            .subtract(RIDE_DEFAULT_FINAL_COST.multiply(BigDecimal.valueOf(BRILLIANT10_DISCOUNT_PERCENT))
                    .divide(BIG_DECIMAL_HUNDRED));
    public static final BigDecimal RIDE_FINAL_COST_WITH_DIAMOND10_PROMO_CODE = RIDE_FINAL_COST_WITH_BRILLIANT10_PROMO_CODE;
    public static final BigDecimal FEE_FOR_RIDE_DEFAULT_FINAL_COST = RIDE_DEFAULT_FINAL_COST
            .multiply(AGGREGATOR_FEE_PERCENT)
            .divide(BIG_DECIMAL_HUNDRED);

    public static final PaymentMethodEnum RIDE_DEFAULT_PAYMENT_METHOD_ENUM = PaymentMethodEnum.BANK_CARD;
    public static final String RIDE_DEFAULT_PAYMENT_METHOD_STRING = RIDE_DEFAULT_PAYMENT_METHOD_ENUM.toString();

    public static final int DEFAULT_PAGEABLE_SIZE = 10;
    public static final String UNSORTED = "UNSORTED";
    public static final String DRIVER_ID_KEY = "driverId";
    public static final String PROMO_CODE_KEYWORD_KEY = "promoCodeKeyword";
    public static final String PAYMENT_METHOD_KEY = "paymentMethod";
    public static final String DRIVER_SCORE_KEY = "driverScore";
    public static final String PASSENGER_SCORE_KEY = "passengerScore";
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

    public static final String CONTRACT_RIDE_PAYMENT_ID = "69b509c6-20fe-4b70-a07e-31e52ea05a54";
    public static final String CONTRACT_RIDE_PAYMENT_PASSENGER_ID = "072f635e-0ee7-461e-aa7e-1901ae3d0c5e";
    public static final String CONTRACT_RIDE_PAYMENT_DRIVER_ID = "d9343856-ad27-4256-9534-4c59fa5e6422";
    public static final String CONTRACT_RIDE_PAYMENT_START_ADDRESS = "Bramcote Grove, 42";
    public static final String CONTRACT_RIDE_PAYMENT_DESTINATION_ADDRESS = "Llewellyn Street, 35";
    public static final BigDecimal CONTRACT_RIDE_PAYMENT_FINAL_COST = BigDecimal.valueOf(100);
    public static final BigDecimal CONTRACT_RIDE_PAYMENT_INITIAL_COST = BigDecimal.valueOf(100);
    public static final String CONTRACT_RIDE_PAYMENT_STATUS = "END_RIDE";
    public static final ZonedDateTime CONTRACT_RIDE_PAYMENT_DEFAULT_TIMESTAMP = ZonedDateTime.of(2024, 4, 4, 12, 2, 0,
            0, EUROPE_MINSK_TIMEZONE);

    public static PromoCodeRequestDto.PromoCodeRequestDtoBuilder getNewPromoCodeRequestDto() {
        return PromoCodeRequestDto.builder()
                .withKeyword(NEW_PROMO_CODE_KEYWORD)
                .withDiscountPercent(NEW_PROMO_CODE_DISCOUNT_PERCENT);
    }

    public static PromoCodeResponseDto.PromoCodeResponseDtoBuilder getAddedPromoCodeResponseDto() {
        return PromoCodeResponseDto.builder()
                .withId(null)
                .withIsActive(NEW_PROMO_CODE_IS_ACTIVE)
                .withKeyword(NEW_PROMO_CODE_KEYWORD)
                .withDiscountPercent(NEW_PROMO_CODE_DISCOUNT_PERCENT);
    }

    public static PromoCodeResponseDto.PromoCodeResponseDtoBuilder getBRILLIANT10ActivePromoCodeResponseDto() {
        return PromoCodeResponseDto.builder()
                .withId(BRILLIANT10_ID)
                .withIsActive(BRILLIANT10_IS_ACTIVE)
                .withKeyword(BRILLIANT10_KEYWORD)
                .withDiscountPercent(BRILLIANT10_DISCOUNT_PERCENT);
    }

    public static PromoCodeResponseDto.PromoCodeResponseDtoBuilder getPAIN49ActivePromoCodeResponseDto() {
        return PromoCodeResponseDto.builder()
                .withId(PAIN49_ACTIVE_ID)
                .withIsActive(PAIN49_ACTIVE_IS_ACTIVE)
                .withKeyword(PAIN49_ACTIVE_KEYWORD)
                .withDiscountPercent(PAIN49_ACTIVE_DISCOUNT_PERCENT);
    }

    public static PromoCodeResponseDto.PromoCodeResponseDtoBuilder getPAIN49NotActivePromoCodeResponseDto() {
        return PromoCodeResponseDto.builder()
                .withId(PAIN49_NOT_ACTIVE_ID)
                .withIsActive(PAIN49_NOT_ACTIVE_IS_ACTIVE)
                .withKeyword(PAIN49_NOT_ACTIVE_KEYWORD)
                .withDiscountPercent(PAIN49_NOT_ACTIVE_PERCENT_DISCOUNT);
    }

    public static PromoCodeResponseDto.PromoCodeResponseDtoBuilder getRICE23NotActivePromoCodeResponseDto() {
        return PromoCodeResponseDto.builder()
                .withId(RICE23_ID)
                .withIsActive(RICE23_IS_ACTIVE)
                .withKeyword(RICE23_KEYWORD)
                .withDiscountPercent(RICE23_DISCOUNT_PERCENT);
    }

    public static PassengerResponseDto.PassengerResponseDtoBuilder getPassengerResponseDto() {
        return PassengerResponseDto.builder()
                .withId(PASSENGER_5_ID)
                .withName(DEFAULT_PASSENGER_NAME)
                .withEmail(DEFAULT_PASSENGER_EMAIL)
                .withCardNumber(DEFAULT_PASSENGER_CARD_NUMBER);
    }

    public static DriverResponseDto.DriverResponseDtoBuilder getDriverResponseDto() {
        return DriverResponseDto.builder()
                .withIsAvailable(DEFAULT_DRIVER_IS_AVAILABLE)
                .withId(DRIVER_1_ID)
                .withName(DEFAULT_DRIVER_NAME)
                .withEmail(DEFAULT_DRIVER_EMAIL)
                .withCardNumber(DEFAULT_DRIVER_CARD_NUMBER);
    }

    public static PassengerPaymentResponseDto.PassengerPaymentResponseDtoBuilder getPassengerPaymentResponseDto() {
        return PassengerPaymentResponseDto.builder()
                .withId(PASSENGER_PAYMENT_DEFAULT_ID)
                .withRideId(ENDED_CASH_NOT_PAID_RIDE_ID)
                .withPassengerId(PASSENGER_4_ID)
                .withDriverId(DRIVER_3_ID)
                .withAmount(RIDE_DEFAULT_FINAL_COST)
                .withFeeAmount(FEE_FOR_RIDE_DEFAULT_FINAL_COST)
                .withPaymentMethod(PaymentMethodEnum.CASH.toString())
                .withCardNumber(null)
                .withStatus(PAYMENT_DEFAULT_STATUS)
                .withTimestamp(PASSENGER_PAYMENT_DEFAULT_TIMESTAMP);
    }

    public static RatingResponseDto.RatingResponseDtoBuilder getPassengerRating() {
        return RatingResponseDto.builder()
                .withUserType(UserTypeEnum.PASSENGER)
                .withUserId(PASSENGER_1_ID)
                .withRating(PASSENGER_1_RATING);
    }

    public static RatingResponseDto.RatingResponseDtoBuilder getDriverRating() {
        return RatingResponseDto.builder()
                .withUserType(UserTypeEnum.DRIVER)
                .withUserId(DRIVER_4_ID)
                .withRating(DRIVER_4_RATING);
    }

    public static RideRequestDto.RideRequestDtoBuilder getNewRideRequestDto() {
        return RideRequestDto.builder()
                .withPassengerId(PASSENGER_5_ID)
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS);
    }

    public static RideResponseDto.RideResponseDtoBuilder getAddedRideResponseDto() {
        return RideResponseDto.builder()
                .withId(null)
                .withInitialCost(null)
                .withFinalCost(null)
                .withPassengerId(PASSENGER_5_ID)
                .withDriverId(null)
                .withPromoCode(null)
                .withStatus(RideStatusEnum.BOOKED.toString())
                .withPaymentMethod(RIDE_DEFAULT_PAYMENT_METHOD_STRING)
                .withIsPaid(false)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(null)
                .withCancelRide(null)
                .withAcceptRide(null)
                .withBeginRide(null)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getBookedRideResponseDto() {
        return RideResponseDto.builder()
                .withId(BOOKED_RIDE_ID)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST)
                .withPassengerId(PASSENGER_1_ID)
                .withDriverId(null)
                .withPromoCode(null)
                .withStatus(RideStatusEnum.BOOKED.toString())
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withIsPaid(false)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(null)
                .withBeginRide(null)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getAcceptedRideResponseDto() {
        return RideResponseDto.builder()
                .withId(ACCEPTED_RIDE_ID)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST)
                .withPassengerId(PASSENGER_2_ID)
                .withDriverId(DRIVER_1_ID)
                .withPromoCode(null)
                .withStatus(RideStatusEnum.ACCEPTED.toString())
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withIsPaid(false)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(null)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getBeganBankCardRideResponseDto() {
        return RideResponseDto.builder()
                .withId(BEGAN_BANK_CARD_RIDE_ID)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST)
                .withPassengerId(PASSENGER_3_ID)
                .withDriverId(DRIVER_2_ID)
                .withPromoCode(null)
                .withStatus(RideStatusEnum.BEGIN_RIDE.toString())
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withIsPaid(false)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getBeganCashRideResponseDto() {
        return RideResponseDto.builder()
                .withId(BEGAN_CASH_RIDE_ID)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST)
                .withPassengerId(PASSENGER_3_ID)
                .withDriverId(DRIVER_2_ID)
                .withPromoCode(null)
                .withStatus(RideStatusEnum.BEGIN_RIDE.toString())
                .withPaymentMethod(PaymentMethodEnum.CASH.toString())
                .withIsPaid(false)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(null)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getEndedCashNotPaidRideResponseDto() {
        return RideResponseDto.builder()
                .withId(ENDED_CASH_NOT_PAID_RIDE_ID)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST)
                .withPassengerId(PASSENGER_4_ID)
                .withDriverId(DRIVER_3_ID)
                .withPromoCode(null)
                .withStatus(RideStatusEnum.END_RIDE.toString())
                .withPaymentMethod(PaymentMethodEnum.CASH.toString())
                .withIsPaid(false)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getEndedBankCardNotPaidRideResponseDto() {
        return RideResponseDto.builder()
                .withId(ENDED_BANK_CARD_NOT_PAID_RIDE_ID)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST)
                .withPassengerId(PASSENGER_4_ID)
                .withDriverId(DRIVER_3_ID)
                .withPromoCode(null)
                .withStatus(RideStatusEnum.END_RIDE.toString())
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withIsPaid(false)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getEndedBankCardPaidRideResponseDto() {
        return RideResponseDto.builder()
                .withId(ENDED_BANK_CARD_PAID_RIDE_ID)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST)
                .withPassengerId(PASSENGER_4_ID)
                .withDriverId(DRIVER_3_ID)
                .withPromoCode(null)
                .withStatus(RideStatusEnum.END_RIDE.toString())
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withIsPaid(true)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(null);
    }

    public static RideResponseDto.RideResponseDtoBuilder getFinishedNoScoresRideResponseDto() {
        return RideResponseDto.builder()
                .withId(FINISHED_NO_SCORES_RIDE_ID)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST)
                .withPassengerId(PASSENGER_1_ID)
                .withDriverId(DRIVER_1_ID)
                .withPromoCode(null)
                .withStatus(RideStatusEnum.FINISHED.toString())
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withIsPaid(true)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(RIDE_DEFAULT_FINISH_RIDE);
    }

    public static RideResponseDto.RideResponseDtoBuilder getFinishedWithScoresRideResponseDto() {
        return RideResponseDto.builder()
                .withId(FINISHED_WITH_SCORES_RIDE_ID)
                .withInitialCost(RIDE_DEFAULT_INITIAL_COST)
                .withFinalCost(RIDE_DEFAULT_FINAL_COST)
                .withPassengerId(PASSENGER_1_ID)
                .withDriverId(DRIVER_4_ID)
                .withPromoCode(null)
                .withStatus(RideStatusEnum.FINISHED.toString())
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withIsPaid(true)
                .withStartAddress(RIDE_DEFAULT_START_ADDRESS)
                .withDestinationAddress(RIDE_DEFAULT_DESTINATION_ADDRESS)
                .withPassengerScore(4)
                .withDriverScore(5)
                .withBookRide(RIDE_DEFAULT_BOOK_RIDE)
                .withCancelRide(null)
                .withAcceptRide(RIDE_DEFAULT_ACCEPT_RIDE)
                .withBeginRide(RIDE_DEFAULT_BEGIN_RIDE)
                .withEndRide(RIDE_DEFAULT_END_RIDE)
                .withFinishRide(RIDE_DEFAULT_FINISH_RIDE);
    }

    public static RideResponseDto.RideResponseDtoBuilder getRideForPayment() {
        return RideResponseDto.builder()
                .withId(CONTRACT_RIDE_PAYMENT_ID)
                .withPassengerId(CONTRACT_RIDE_PAYMENT_PASSENGER_ID)
                .withDriverId(CONTRACT_RIDE_PAYMENT_DRIVER_ID)
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withStartAddress(CONTRACT_RIDE_PAYMENT_START_ADDRESS)
                .withDestinationAddress(CONTRACT_RIDE_PAYMENT_DESTINATION_ADDRESS)
                .withInitialCost(CONTRACT_RIDE_PAYMENT_INITIAL_COST)
                .withFinalCost(CONTRACT_RIDE_PAYMENT_FINAL_COST)
                .withIsPaid(false)
                .withStatus(CONTRACT_RIDE_PAYMENT_STATUS)
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(CONTRACT_RIDE_PAYMENT_DEFAULT_TIMESTAMP)
                .withCancelRide(null)
                .withAcceptRide(CONTRACT_RIDE_PAYMENT_DEFAULT_TIMESTAMP)
                .withBeginRide(CONTRACT_RIDE_PAYMENT_DEFAULT_TIMESTAMP)
                .withEndRide(CONTRACT_RIDE_PAYMENT_DEFAULT_TIMESTAMP)
                .withFinishRide(null);
    }

    public static <T> ListContainerResponseDto.ListContainerResponseDtoBuilder<T> getListContainerForResponse(
            Class<T> clazz) {
        return ListContainerResponseDto.<T>builder()
                .withValues(Collections.emptyList())
                .withCurrentPage(0)
                .withSize(DEFAULT_PAGEABLE_SIZE)
                .withLastPage(0)
                .withSort(UNSORTED);
    }
}
