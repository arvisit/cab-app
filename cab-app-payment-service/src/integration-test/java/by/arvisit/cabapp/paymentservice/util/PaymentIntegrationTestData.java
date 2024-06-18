package by.arvisit.cabapp.paymentservice.util;

import static by.arvisit.cabapp.common.util.CommonConstants.EUROPE_MINSK_TIMEZONE;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.common.dto.rides.RideResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaymentIntegrationTestData {

    public static final String DRIVER_REPAYMENT_10_ID = "e64f3f82-83dd-4fd8-8155-c5ed1b100dc1";
    public static final String DRIVER_WITHDRAWAL_50_ID = "761212b8-4796-4e99-a901-e93b5b64a3d9";
    public static final String DRIVER_WITHDRAWAL_100_ID = "d470f2cd-468a-496c-9298-7035c7ca7495";
    public static final String RIDE_1_ID = "942d8839-7a16-4dfa-9443-179220135b3a";
    public static final String RIDE_3_ID = "9ea8a8b0-004b-4ded-8a3a-edc198c89e31";
    public static final String RIDE_2_ID = "b4de939f-3a3f-441c-b081-dc604f6bed20";
    public static final String RIDE_4_ID = "aba3e655-a8b1-4dca-a4b5-6566dd74a47e";
    public static final String PASSENGER_PAYMENT_1_ID = "cce748fb-1a3a-468e-a49e-08a26fe2a418";
    public static final String PASSENGER_PAYMENT_2_ID = "69ee1ad8-f630-445e-964e-0301ffaaa34b";
    public static final String PASSENGER_PAYMENT_3_ID = "d884a140-88d6-4cf4-a6a6-8dfb7e31fd96";
    public static final String PASSENGER_PAYMENT_4_ID = "26a05239-f86b-4c45-9b03-1a24c9070a23";

    public static final BigDecimal DRIVER_ACCOUNT_BALANCE = BigDecimal.valueOf(40);
    public static final BigDecimal DRIVER_ACCOUNT_BALANCE_AFTER_WITHDRAWAL = BigDecimal.valueOf(20);
    public static final BigDecimal DRIVER_ACCOUNT_BALANCE_AFTER_REPAYMENT = BigDecimal.valueOf(60);
    public static final BigDecimal DRIVER_PAYMENT_AMOUNT = BigDecimal.valueOf(20);
    public static final BigDecimal PASSENGER_PAYMENT_FEE_AMOUNT = BigDecimal.valueOf(5);
    public static final BigDecimal PASSENGER_PAYMENT_AMOUNT = BigDecimal.valueOf(100);

    public static final String PASSENGER_ID_STRING = "072f635e-0ee7-461e-aa7e-1901ae3d0c5e";
    public static final String PASSENGER_CARD_NUMBER = "5538411806914853";
    public static final String PASSENGER_EMAIL = "vivienne.gutierrez@yahoo.com.ar";
    public static final String PASSENGER_NAME = "Vivienne Gutierrez";

    public static final String DRIVER_ID_STRING = "d9343856-ad27-4256-9534-4c59fa5e6422";
    public static final String DRIVER_NAME = "Jeremias Olsen";
    public static final String DRIVER_EMAIL = "jeremias.olsen@frontiernet.net";
    public static final String DRIVER_CARD_NUMBER = "8972821332297027";

    public static final String NEW_RIDE_ID_STRING = "69b509c6-20fe-4b70-a07e-31e52ea05a54";
    public static final String RIDE_DESTINATION_ADDRESS = "Llewellyn Street, 35";
    public static final String RIDE_START_ADDRESS = "Bramcote Grove, 42";
    public static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.of(2024, 4, 4, 12, 2, 0, 0,
            EUROPE_MINSK_TIMEZONE);

    public static final String END_RIDE_STATUS = "END_RIDE";
    public static final int DEFAULT_PAGEABLE_SIZE = 10;
    public static final String UNSORTED = "UNSORTED";

    public static final String URL_DRIVER_PAYMENTS = "/api/v1/driver-payments";
    public static final String URL_DRIVER_PAYMENTS_ID_TEMPLATE = "/api/v1/driver-payments/{id}";
    public static final String URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE = "/api/v1/driver-payments/drivers/{id}/balance";
    public static final String URL_PASSENGER_PAYMENTS = "/api/v1/passenger-payments";
    public static final String URL_PASSENGER_PAYMENTS_ID_TEMPLATE = "/api/v1/passenger-payments/{id}";

    public static final String DRIVER_ID_REQUEST_PARAM = "driverId";
    public static final String OPERATION_REQUEST_PARAM = "operation";
    public static final String PAYMENT_METHOD_REQUEST_PARAM = "paymentMethod";

    public static final ZonedDateTime CONTRACT_PASSENGER_PAYMENT_RIDES_TIMESTAMP = ZonedDateTime.of(2024, 4, 4, 12, 6,
            0, 0, EUROPE_MINSK_TIMEZONE);
    public static final String CONTRACT_PASSENGER_PAYMENT_RIDES_STATUS = "SUCCESS";
    public static final String CONTRACT_PASSENGER_PAYMENT_RIDES_CARD_NUMBER = null;
    public static final String CONTRACT_PASSENGER_PAYMENT_RIDES_PAYMENT_METHOD = "CASH";
    public static final BigDecimal CONTRACT_PASSENGER_PAYMENT_RIDES_FEE_AMOUNT = BigDecimal.valueOf(5);
    public static final BigDecimal CONTRACT_PASSENGER_PAYMENT_RIDES_AMOUNT = BigDecimal.valueOf(100);
    public static final String CONTRACT_PASSENGER_PAYMENT_RIDES_DRIVER_ID = "d9343856-ad27-4256-9534-4c59fa5e6422";
    public static final String CONTRACT_PASSENGER_PAYMENT_RIDES_PASSENGER_ID = "deecaeef-454b-487d-987c-54df212385b3";
    public static final String CONTRACT_PASSENGER_PAYMENT_RIDES_RIDE_ID = "ffe34487-dfa3-4660-96dc-ed108e06ab77";
    public static final String CONTRACT_PASSENGER_PAYMENT_RIDES_ID = "cce748fb-1a3a-468e-a49e-08a26fe2a418";

    public static PassengerResponseDto.PassengerResponseDtoBuilder getPassengerResponseDto() {
        return PassengerResponseDto.builder()
                .withId(PASSENGER_ID_STRING)
                .withName(PASSENGER_NAME)
                .withEmail(PASSENGER_EMAIL)
                .withCardNumber(PASSENGER_CARD_NUMBER);
    }

    public static DriverResponseDto.DriverResponseDtoBuilder getDriverResponseDto() {
        return DriverResponseDto.builder()
                .withId(DRIVER_ID_STRING)
                .withName(DRIVER_NAME)
                .withEmail(DRIVER_EMAIL)
                .withCardNumber(DRIVER_CARD_NUMBER)
                .withCar(null)
                .withIsAvailable(true);
    }

    public static RideResponseDto.RideResponseDtoBuilder getRideResponseDto() {
        return RideResponseDto.builder()
                .withId(NEW_RIDE_ID_STRING)
                .withPassengerId(PASSENGER_ID_STRING)
                .withDriverId(DRIVER_ID_STRING)
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withStartAddress(RIDE_START_ADDRESS)
                .withDestinationAddress(RIDE_DESTINATION_ADDRESS)
                .withInitialCost(BigDecimal.valueOf(100))
                .withFinalCost(BigDecimal.valueOf(100))
                .withIsPaid(false)
                .withStatus(END_RIDE_STATUS)
                .withPromoCode(null)
                .withPassengerScore(null)
                .withDriverScore(null)
                .withBookRide(DEFAULT_TIMESTAMP)
                .withCancelRide(null)
                .withAcceptRide(DEFAULT_TIMESTAMP)
                .withBeginRide(DEFAULT_TIMESTAMP)
                .withEndRide(DEFAULT_TIMESTAMP)
                .withFinishRide(null);
    }

    public static PassengerPaymentRequestDto.PassengerPaymentRequestDtoBuilder getPassengerPaymentRequestDto() {
        return PassengerPaymentRequestDto.builder()
                .withRideId(NEW_RIDE_ID_STRING)
                .withPassengerId(PASSENGER_ID_STRING)
                .withDriverId(DRIVER_ID_STRING)
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withCardNumber(PASSENGER_CARD_NUMBER)
                .withAmount(PASSENGER_PAYMENT_AMOUNT);
    }

    public static DriverPaymentRequestDto.DriverPaymentRequestDtoBuilder getDriverPaymentRequestDto() {
        return DriverPaymentRequestDto.builder()
                .withDriverId(DRIVER_ID_STRING)
                .withOperation(OperationTypeEnum.WITHDRAWAL.toString())
                .withCardNumber(DRIVER_CARD_NUMBER)
                .withAmount(DRIVER_PAYMENT_AMOUNT);
    }

    public static PassengerPaymentResponseDto.PassengerPaymentResponseDtoBuilder getAddedPassengerPaymentResponseDto() {
        return PassengerPaymentResponseDto.builder()
                .withId(null)
                .withRideId(NEW_RIDE_ID_STRING)
                .withPassengerId(PASSENGER_ID_STRING)
                .withDriverId(DRIVER_ID_STRING)
                .withAmount(PASSENGER_PAYMENT_AMOUNT)
                .withFeeAmount(PASSENGER_PAYMENT_FEE_AMOUNT)
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withCardNumber(PASSENGER_CARD_NUMBER)
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withTimestamp(null);
    }

    public static DriverPaymentResponseDto.DriverPaymentResponseDtoBuilder getAddedDriverPaymentResponseDto() {
        return DriverPaymentResponseDto.builder()
                .withId(null)
                .withDriverId(DRIVER_ID_STRING)
                .withOperation(OperationTypeEnum.WITHDRAWAL.toString())
                .withCardNumber(DRIVER_CARD_NUMBER)
                .withAmount(DRIVER_PAYMENT_AMOUNT)
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withTimestamp(null);
    }

    public static PassengerPaymentResponseDto.PassengerPaymentResponseDtoBuilder getPassengerPaymentResponseDto1() {
        return PassengerPaymentResponseDto.builder()
                .withId(PASSENGER_PAYMENT_1_ID)
                .withRideId(RIDE_1_ID)
                .withPassengerId(PASSENGER_ID_STRING)
                .withDriverId(DRIVER_ID_STRING)
                .withAmount(BigDecimal.valueOf(100))
                .withFeeAmount(BigDecimal.valueOf(5))
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withCardNumber(PASSENGER_CARD_NUMBER)
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withTimestamp(ZonedDateTime.of(2024, 4, 4, 12, 1, 0, 0, EUROPE_MINSK_TIMEZONE));
    }

    public static PassengerPaymentResponseDto.PassengerPaymentResponseDtoBuilder getPassengerPaymentResponseDto2() {
        return PassengerPaymentResponseDto.builder()
                .withId(PASSENGER_PAYMENT_2_ID)
                .withRideId(RIDE_2_ID)
                .withPassengerId(PASSENGER_ID_STRING)
                .withDriverId(DRIVER_ID_STRING)
                .withAmount(BigDecimal.valueOf(100))
                .withFeeAmount(BigDecimal.valueOf(5))
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withCardNumber(PASSENGER_CARD_NUMBER)
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withTimestamp(ZonedDateTime.of(2024, 4, 4, 12, 2, 0, 0, EUROPE_MINSK_TIMEZONE));
    }

    public static PassengerPaymentResponseDto.PassengerPaymentResponseDtoBuilder getPassengerPaymentResponseDto3() {
        return PassengerPaymentResponseDto.builder()
                .withId(PASSENGER_PAYMENT_3_ID)
                .withRideId(RIDE_3_ID)
                .withPassengerId(PASSENGER_ID_STRING)
                .withDriverId(DRIVER_ID_STRING)
                .withAmount(BigDecimal.valueOf(100))
                .withFeeAmount(BigDecimal.valueOf(5))
                .withPaymentMethod(PaymentMethodEnum.CASH.toString())
                .withCardNumber(null)
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withTimestamp(ZonedDateTime.of(2024, 4, 4, 12, 3, 0, 0, EUROPE_MINSK_TIMEZONE));
    }

    public static PassengerPaymentResponseDto.PassengerPaymentResponseDtoBuilder getPassengerPaymentResponseDto4() {
        return PassengerPaymentResponseDto.builder()
                .withId(PASSENGER_PAYMENT_4_ID)
                .withRideId(RIDE_4_ID)
                .withPassengerId(PASSENGER_ID_STRING)
                .withDriverId(DRIVER_ID_STRING)
                .withAmount(BigDecimal.valueOf(100))
                .withFeeAmount(BigDecimal.valueOf(5))
                .withPaymentMethod(PaymentMethodEnum.CASH.toString())
                .withCardNumber(null)
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withTimestamp(ZonedDateTime.of(2024, 4, 4, 12, 4, 0, 0, EUROPE_MINSK_TIMEZONE));
    }

    public static DriverPaymentResponseDto.DriverPaymentResponseDtoBuilder getDriverWithdrawal100PaymentResponseDto() {
        return DriverPaymentResponseDto.builder()
                .withId(DRIVER_WITHDRAWAL_100_ID)
                .withDriverId(DRIVER_ID_STRING)
                .withOperation(OperationTypeEnum.WITHDRAWAL.toString())
                .withCardNumber(DRIVER_CARD_NUMBER)
                .withAmount(BigDecimal.valueOf(100))
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withTimestamp(ZonedDateTime.of(2024, 4, 4, 12, 5, 0, 0, EUROPE_MINSK_TIMEZONE));
    }

    public static DriverPaymentResponseDto.DriverPaymentResponseDtoBuilder getDriverWithdrawal50PaymentResponseDto() {
        return DriverPaymentResponseDto.builder()
                .withId(DRIVER_WITHDRAWAL_50_ID)
                .withDriverId(DRIVER_ID_STRING)
                .withOperation(OperationTypeEnum.WITHDRAWAL.toString())
                .withCardNumber(DRIVER_CARD_NUMBER)
                .withAmount(BigDecimal.valueOf(50))
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withTimestamp(ZonedDateTime.of(2024, 4, 4, 12, 6, 0, 0, EUROPE_MINSK_TIMEZONE));
    }

    public static DriverPaymentResponseDto.DriverPaymentResponseDtoBuilder getDriverRepayment10PaymentResponseDto() {
        return DriverPaymentResponseDto.builder()
                .withId(DRIVER_REPAYMENT_10_ID)
                .withDriverId(DRIVER_ID_STRING)
                .withOperation(OperationTypeEnum.REPAYMENT.toString())
                .withCardNumber(DRIVER_CARD_NUMBER)
                .withAmount(BigDecimal.valueOf(10))
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withTimestamp(ZonedDateTime.of(2024, 4, 4, 12, 7, 0, 0, EUROPE_MINSK_TIMEZONE));
    }

    public static DriverAccountBalanceResponseDto.DriverAccountBalanceResponseDtoBuilder getDriverAccountBalanceResponseDto() {
        return DriverAccountBalanceResponseDto.builder()
                .withDriverId(DRIVER_ID_STRING)
                .withBalance(DRIVER_ACCOUNT_BALANCE);
    }

    public static PassengerPaymentRequestDto.PassengerPaymentRequestDtoBuilder getPassengerPaymentRequestToSaveFromRides() {
        return PassengerPaymentRequestDto.builder()
                .withRideId(CONTRACT_PASSENGER_PAYMENT_RIDES_RIDE_ID)
                .withPassengerId(CONTRACT_PASSENGER_PAYMENT_RIDES_PASSENGER_ID)
                .withDriverId(CONTRACT_PASSENGER_PAYMENT_RIDES_DRIVER_ID)
                .withPaymentMethod(CONTRACT_PASSENGER_PAYMENT_RIDES_PAYMENT_METHOD)
                .withCardNumber(CONTRACT_PASSENGER_PAYMENT_RIDES_CARD_NUMBER)
                .withAmount(CONTRACT_PASSENGER_PAYMENT_RIDES_AMOUNT);
    }

    public static PassengerPaymentResponseDto.PassengerPaymentResponseDtoBuilder getSavedPassengerPaymentForRides() {
        return PassengerPaymentResponseDto.builder()
                .withId(CONTRACT_PASSENGER_PAYMENT_RIDES_ID)
                .withRideId(CONTRACT_PASSENGER_PAYMENT_RIDES_RIDE_ID)
                .withPassengerId(CONTRACT_PASSENGER_PAYMENT_RIDES_PASSENGER_ID)
                .withDriverId(CONTRACT_PASSENGER_PAYMENT_RIDES_DRIVER_ID)
                .withAmount(CONTRACT_PASSENGER_PAYMENT_RIDES_AMOUNT)
                .withFeeAmount(CONTRACT_PASSENGER_PAYMENT_RIDES_FEE_AMOUNT)
                .withPaymentMethod(CONTRACT_PASSENGER_PAYMENT_RIDES_PAYMENT_METHOD)
                .withCardNumber(CONTRACT_PASSENGER_PAYMENT_RIDES_CARD_NUMBER)
                .withStatus(CONTRACT_PASSENGER_PAYMENT_RIDES_STATUS)
                .withTimestamp(CONTRACT_PASSENGER_PAYMENT_RIDES_TIMESTAMP);
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
