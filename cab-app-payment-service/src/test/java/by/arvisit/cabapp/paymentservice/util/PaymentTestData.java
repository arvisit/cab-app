package by.arvisit.cabapp.paymentservice.util;

import static by.arvisit.cabapp.common.util.CommonConstants.EUROPE_MINSK_TIMEZONE;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.common.dto.rides.RideResponseDto;
import by.arvisit.cabapp.common.util.DateRange;
import by.arvisit.cabapp.paymentservice.dto.DriverAccountBalanceResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentsFilterParams;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentsFilterParams;
import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment;
import by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum;
import by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaymentTestData {

    public static final BigDecimal DRIVER_ACCOUNT_BALANCE_ENOUGH = BigDecimal.valueOf(1000);
    public static final BigDecimal DRIVER_ACCOUNT_BALANCE_NOT_ENOUGH = BigDecimal.valueOf(10);
    public static final BigDecimal DRIVER_ACCOUNT_BALANCE_NEGATIVE = BigDecimal.valueOf(-10);
    public static final String DRIVER_PAYMENT_ID_STRING = "23904afd-a2af-4e04-a605-b2d375ab8ca4";
    public static final UUID DRIVER_PAYMENT_ID_UUID = UUID.fromString(DRIVER_PAYMENT_ID_STRING);
    public static final String DRIVER_CARD_NUMBER = "1522613953683617";
    public static final BigDecimal DRIVER_PAYMENT_AMOUNT = BigDecimal.valueOf(50);
    public static final String PASSENGER_PAYMENT_ID_STRING = "cce748fb-1a3a-468e-a49e-08a26fe2a418";
    public static final UUID PASSENGER_PAYMENT_ID_UUID = UUID.fromString(PASSENGER_PAYMENT_ID_STRING);
    public static final BigDecimal PASSENGER_PAYMENT_FEE_AMOUNT = BigDecimal.valueOf(5);
    public static final BigDecimal PASSENGER_PAYMENT_AMOUNT = BigDecimal.valueOf(100);
    public static final BigDecimal PASSENGER_PAYMENT_NEGATIVE_AMOUNT = BigDecimal.valueOf(-100);
    public static final String PASSENGER_CARD_NUMBER = "7853471929691513";
    public static final String PASSENGER_EMAIL = "vivienne.gutierrez@yahoo.com.ar";
    public static final String PASSENGER_NAME = "Vivienne Gutierrez";
    public static final String DRIVER_ID_STRING = "a7dd0543-0adc-4ea6-9ca7-7b72065ca011";
    public static final String DRIVER_NAME = "Jeremias Olsen";
    public static final String DRIVER_EMAIL = "jeremias.olsen@frontiernet.net";
    public static final String ANOTHER_DRIVER_ID_STRING = "a7cd0543-0adc-4ea6-9ca7-7b72065ca011";
    public static final UUID ANOTHER_DRIVER_ID_UUID = UUID.fromString(ANOTHER_DRIVER_ID_STRING);
    public static final UUID DRIVER_ID_UUID = UUID.fromString(DRIVER_ID_STRING);
    public static final String PASSENGER_ID_STRING = "3abcc6a1-94da-4185-aaa1-8a11c1b8efd2";
    public static final String ANOTHER_PASSENGER_ID_STRING = "3abdc6a1-94da-4185-aaa1-8a11c1b8efd2";
    public static final UUID ANOTHER_PASSENGER_ID_UUID = UUID.fromString(ANOTHER_PASSENGER_ID_STRING);;
    public static final UUID PASSENGER_ID_UUID = UUID.fromString(PASSENGER_ID_STRING);
    public static final String RIDE_ID_STRING = "a5b0a1f9-f45d-4287-bbad-ea6253976d0d";
    public static final UUID RIDE_ID_UUID = UUID.fromString(RIDE_ID_STRING);
    public static final String RIDE_DESTINATION_ADDRESS = "Llewellyn Street, 35";
    public static final String RIDE_START_ADDRESS = "Bramcote Grove, 42";
    public static final BigDecimal RIDE_INITIAL_COST = BigDecimal.valueOf(100);
    public static final BigDecimal RIDE_FINAL_COST = BigDecimal.valueOf(100);
    public static final ZonedDateTime PASSENGER_PAYMENT_DEFAULT_TIMESTAMP = ZonedDateTime.of(2024, 4, 4, 12, 0, 0, 0,
            EUROPE_MINSK_TIMEZONE);
    public static final ZonedDateTime DRIVER_PAYMENT_DEFAULT_TIMESTAMP = ZonedDateTime.of(2024, 4, 4, 12, 1, 0, 0,
            EUROPE_MINSK_TIMEZONE);
    public static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.of(2024, 4, 4, 12, 2, 0, 0,
            EUROPE_MINSK_TIMEZONE);
    public static final int DEFAULT_PAGEABLE_SIZE = 10;
    public static final String UNSORTED = "UNSORTED";
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String NOT_VALID_OPERATION = "DEBT";
    public static final String NOT_VALID_PAYMENT_METHOD = "DIAMONDS";
    public static final String URL_DRIVER_PAYMENTS = "/api/v1/driver-payments";
    public static final String URL_DRIVER_PAYMENTS_ID_TEMPLATE = "/api/v1/driver-payments/{id}";
    public static final String URL_DRIVER_PAYMENTS_PARAM_VALUE_TEMPLATE = "/api/v1/driver-payments?{param}={value}";
    public static final String URL_DRIVER_PAYMENTS_DRIVERS_ID_BALANCE_TEMPLATE = "/api/v1/driver-payments/drivers/{id}/balance";
    public static final String URL_PASSENGER_PAYMENTS = "/api/v1/passenger-payments";
    public static final String URL_PASSENGER_PAYMENTS_ID_TEMPLATE = "/api/v1/passenger-payments/{id}";
    public static final String URL_PASSENGER_PAYMENTS_PARAM_VALUE_TEMPLATE = "/api/v1/passenger-payments?{param}={value}";
    
    public static final String NOT_ALLOWED_REQUEST_PARAM = "noSuchParam";
    public static final String DRIVER_ID_REQUEST_PARAM = "driverId";
    public static final String PASSENGER_ID_REQUEST_PARAM = "passengerId";
    public static final String TIMESTAMP_REQUEST_PARAM = "timestamp";
    public static final String RIDE_ID_REQUEST_PARAM = "rideId";
    public static final String STATUS_REQUEST_PARAM = "status";
    public static final String PAYMENT_METHOD_REQUEST_PARAM = "paymentMethod";
    public static final String OPERATION_REQUEST_PARAM = "operation";

    public static final String DATE_REQUEST_VALUE = "2024-01-02";
    public static final DateRange DATE_RANGE_FROM_REQUEST = DateRange.fromSingleValue(DATE_REQUEST_VALUE);

    public static PassengerPaymentRequestDto.PassengerPaymentRequestDtoBuilder getPassengerPaymentRequestDto() {
        return PassengerPaymentRequestDto.builder()
                .withRideId(RIDE_ID_STRING)
                .withPassengerId(PASSENGER_ID_STRING)
                .withDriverId(DRIVER_ID_STRING)
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withCardNumber(PASSENGER_CARD_NUMBER)
                .withAmount(PASSENGER_PAYMENT_AMOUNT);
    }

    public static PassengerPayment.PassengerPaymentBuilder getPassengerPayment() {
        return PassengerPayment.builder()
                .withId(PASSENGER_PAYMENT_ID_UUID)
                .withRideId(RIDE_ID_UUID)
                .withPassengerId(PASSENGER_ID_UUID)
                .withDriverId(DRIVER_ID_UUID)
                .withAmount(PASSENGER_PAYMENT_AMOUNT)
                .withFeeAmount(PASSENGER_PAYMENT_FEE_AMOUNT)
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD)
                .withCardNumber(PASSENGER_CARD_NUMBER)
                .withStatus(PaymentStatusEnum.SUCCESS)
                .withTimestamp(PASSENGER_PAYMENT_DEFAULT_TIMESTAMP);
    }

    public static PassengerPaymentResponseDto.PassengerPaymentResponseDtoBuilder getPassengerPaymentResponseDto() {
        return PassengerPaymentResponseDto.builder()
                .withId(PASSENGER_PAYMENT_ID_STRING)
                .withRideId(RIDE_ID_STRING)
                .withPassengerId(PASSENGER_ID_STRING)
                .withDriverId(DRIVER_ID_STRING)
                .withAmount(PASSENGER_PAYMENT_AMOUNT)
                .withFeeAmount(PASSENGER_PAYMENT_FEE_AMOUNT)
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withCardNumber(PASSENGER_CARD_NUMBER)
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withTimestamp(PASSENGER_PAYMENT_DEFAULT_TIMESTAMP);
    }

    public static DriverPaymentRequestDto.DriverPaymentRequestDtoBuilder getDriverPaymentRequestDto() {
        return DriverPaymentRequestDto.builder()
                .withDriverId(DRIVER_ID_STRING)
                .withOperation(OperationTypeEnum.WITHDRAWAL.toString())
                .withCardNumber(DRIVER_CARD_NUMBER)
                .withAmount(DRIVER_PAYMENT_AMOUNT);
    }

    public static DriverPayment.DriverPaymentBuilder getDriverPayment() {
        return DriverPayment.builder()
                .withId(DRIVER_PAYMENT_ID_UUID)
                .withDriverId(DRIVER_ID_UUID)
                .withOperation(OperationTypeEnum.WITHDRAWAL)
                .withCardNumber(DRIVER_CARD_NUMBER)
                .withAmount(DRIVER_PAYMENT_AMOUNT)
                .withStatus(PaymentStatusEnum.SUCCESS)
                .withTimestamp(DRIVER_PAYMENT_DEFAULT_TIMESTAMP);
    }

    public static DriverPaymentResponseDto.DriverPaymentResponseDtoBuilder getDriverPaymentResponseDto() {
        return DriverPaymentResponseDto.builder()
                .withId(DRIVER_PAYMENT_ID_STRING)
                .withDriverId(DRIVER_ID_STRING)
                .withOperation(OperationTypeEnum.WITHDRAWAL.toString())
                .withCardNumber(DRIVER_CARD_NUMBER)
                .withAmount(DRIVER_PAYMENT_AMOUNT)
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withTimestamp(DRIVER_PAYMENT_DEFAULT_TIMESTAMP);
    }

    public static DriverAccountBalanceResponseDto.DriverAccountBalanceResponseDtoBuilder getDriverAccountBalanceResponseDto() {
        return DriverAccountBalanceResponseDto.builder()
                .withDriverId(DRIVER_ID_STRING)
                .withBalance(DRIVER_ACCOUNT_BALANCE_ENOUGH);
    }

    public static PassengerResponseDto.PassengerResponseDtoBuilder getPassengerResponseDto() {
        return PassengerResponseDto.builder()
                .withId(PASSENGER_ID_STRING)
                .withName(PASSENGER_NAME)
                .withEmail(PASSENGER_EMAIL)
                .withCardNumber(PASSENGER_CARD_NUMBER);
    }
    
    public static RideResponseDto.RideResponseDtoBuilder getEndedRideResponseDto() {
        return RideResponseDto.builder()
                .withId(RIDE_ID_STRING)
                .withPassengerId(PASSENGER_ID_STRING)
                .withDriverId(DRIVER_ID_STRING)
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withStartAddress(RIDE_START_ADDRESS)
                .withDestinationAddress(RIDE_DESTINATION_ADDRESS)
                .withInitialCost(RIDE_INITIAL_COST)
                .withFinalCost(RIDE_FINAL_COST)
                .withIsPaid(false)
                .withStatus("END_RIDE")
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
    
    public static DriverResponseDto.DriverResponseDtoBuilder getDriverResponseDto() {
        return DriverResponseDto.builder()
                .withId(DRIVER_ID_STRING)
                .withName(DRIVER_NAME)
                .withEmail(DRIVER_EMAIL)
                .withCardNumber(DRIVER_CARD_NUMBER)
                .withCar(null)
                .withIsAvailable(true);
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

    public static PassengerPaymentsFilterParams.PassengerPaymentsFilterParamsBuilder getEmptyPassengerPaymentsFilterParams() {
        return PassengerPaymentsFilterParams.builder()
                .withStatus(null)
                .withPaymentMethod(null)
                .withPassengerId(null)
                .withDriverId(null)
                .withRideId(null)
                .withTimestamp(null);
    }

    public static PassengerPaymentsFilterParams.PassengerPaymentsFilterParamsBuilder getFilledPassengerPaymentsFilterParams() {
        return PassengerPaymentsFilterParams.builder()
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD.toString())
                .withPassengerId(PASSENGER_ID_UUID)
                .withDriverId(DRIVER_ID_UUID)
                .withRideId(RIDE_ID_UUID)
                .withTimestamp(DATE_RANGE_FROM_REQUEST);
    }

    public static Map<String, String> getPassengerPaymentsRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(STATUS_REQUEST_PARAM, PaymentStatusEnum.SUCCESS.toString());
        params.put(PAYMENT_METHOD_REQUEST_PARAM, PaymentMethodEnum.BANK_CARD.toString());
        params.put(PASSENGER_ID_REQUEST_PARAM, PASSENGER_ID_STRING);
        params.put(DRIVER_ID_REQUEST_PARAM, DRIVER_ID_STRING);
        params.put(RIDE_ID_REQUEST_PARAM, RIDE_ID_STRING);
        params.put(TIMESTAMP_REQUEST_PARAM, DATE_REQUEST_VALUE);
        return params;
    }

    public static DriverPaymentsFilterParams.DriverPaymentsFilterParamsBuilder getEmptyDriverPaymentsFilterParams() {
        return DriverPaymentsFilterParams.builder()
                .withStatus(null)
                .withOperation(null)
                .withDriverId(null)
                .withTimestamp(null);
    }

    public static DriverPaymentsFilterParams.DriverPaymentsFilterParamsBuilder getFilledDriverPaymentsFilterParams() {
        return DriverPaymentsFilterParams.builder()
                .withStatus(PaymentStatusEnum.SUCCESS.toString())
                .withOperation(OperationTypeEnum.WITHDRAWAL.toString())
                .withDriverId(DRIVER_ID_UUID)
                .withTimestamp(DATE_RANGE_FROM_REQUEST);
    }

    public static Map<String, String> getDriverPaymentsRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(STATUS_REQUEST_PARAM, PaymentStatusEnum.SUCCESS.toString());
        params.put(OPERATION_REQUEST_PARAM, OperationTypeEnum.WITHDRAWAL.toString());
        params.put(DRIVER_ID_REQUEST_PARAM, DRIVER_ID_STRING);
        params.put(TIMESTAMP_REQUEST_PARAM, DATE_REQUEST_VALUE);
        return params;
    }

    public static Stream<String> blankStrings() {
        return Stream.of("", "   ", null);
    }

    public static Stream<String> malformedUUIDs() {
        return Stream.of("3abcc6a1-94da-4185-1-8a11c1b8efd2", "3abcc6a1-94da-4185-aaa1-8a11c1b8efdw",
                "3ABCC6A1-94DA-4185-AAA1-8A11C1B8EFD2", "   ", "1234", "abc", "111122223333444a",
                "111122223333-444");
    }

    public static Stream<String> malformedDates() {
        return Stream.of("", "   ", "abc", "111122223333444a", "111122223333-444", "truth", "lies", "trui",
                "falce", "232", "2004-18", "23-121", "2004-12-88", "2004-11-11T25", "2004-11-11 11");
    }

    public static Stream<String> malformedCardNumbers() {
        return Stream.of("", "   ", "1234", "abc", "111122223333444a", "111122223333-444");
    }
}
