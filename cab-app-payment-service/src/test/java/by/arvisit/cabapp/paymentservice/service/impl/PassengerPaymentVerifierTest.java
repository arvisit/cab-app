package by.arvisit.cabapp.paymentservice.service.impl;

import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.ANOTHER_DRIVER_ID_UUID;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.ANOTHER_PASSENGER_ID_UUID;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.DRIVER_CARD_NUMBER;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.DRIVER_PAYMENT_AMOUNT;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.PASSENGER_CARD_NUMBER;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getEndedRideResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getPassengerPayment;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getPassengerResponseDto;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.common.dto.rides.RideResponseDto;
import by.arvisit.cabapp.paymentservice.client.PassengerClient;
import by.arvisit.cabapp.paymentservice.client.RideClient;
import by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.paymentservice.persistence.repository.PassengerPaymentRepository;

@ExtendWith(MockitoExtension.class)
class PassengerPaymentVerifierTest {

    @InjectMocks
    private PassengerPaymentVerifier passengerPaymentVerifier;
    @Mock
    private PassengerPaymentRepository passengerPaymentRepository;
    @Mock
    private MessageSource messageSource;
    @Mock
    private PassengerClient passengerClient;
    @Mock
    private RideClient rideClient;

    @Test
    void shouldThrowNoException_whenVerifyNewPaymentWithValidPayment() {
        PassengerPaymentVerifier verifierSpy = spy(passengerPaymentVerifier);

        doNothing().when(verifierSpy)
                .verifyNewPaymentParametersCombination(any(PassengerPayment.class));
        doNothing().when(verifierSpy)
                .verifyIfSameSuccessfulPaymentExists(any(PassengerPayment.class));
        doNothing().when(verifierSpy)
                .verifyPassenger(any(PassengerPayment.class));
        doNothing().when(verifierSpy)
                .verifyRide(any(PassengerPayment.class));

        PassengerPayment payment = getPassengerPayment().build();

        assertThatNoException().isThrownBy(() -> verifierSpy.verifyNewPayment(payment));
    }

    @Test
    void shouldThrowNoException_whenVerifyNewPaymentParametersCombinationWithNoPaymentsForCurrentRide() {
        List<PassengerPayment> paymentsForCurrentRide = Collections.emptyList();

        when(passengerPaymentRepository.findByRideId(any(UUID.class)))
                .thenReturn(paymentsForCurrentRide);

        PassengerPayment payment = getPassengerPayment().build();

        assertThatNoException()
                .isThrownBy(() -> passengerPaymentVerifier.verifyNewPaymentParametersCombination(payment));
    }

    @Test
    void shouldThrowNoException_whenVerifyNewPaymentParametersCombinationWithSamePassengerAndDriverForCurrentRide() {
        PassengerPayment payment = getPassengerPayment().build();
        List<PassengerPayment> paymentsForCurrentRide = List.of(payment);

        when(passengerPaymentRepository.findByRideId(any(UUID.class)))
                .thenReturn(paymentsForCurrentRide);

        assertThatNoException()
                .isThrownBy(() -> passengerPaymentVerifier.verifyNewPaymentParametersCombination(payment));
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyNewPaymentParametersCombinationWithDifferentPassengerForCurrentRide() {
        PassengerPayment currentRidePayment = getPassengerPayment()
                .withPassengerId(ANOTHER_PASSENGER_ID_UUID)
                .build();
        List<PassengerPayment> paymentsForCurrentRide = List.of(currentRidePayment);

        when(passengerPaymentRepository.findByRideId(any(UUID.class)))
                .thenReturn(paymentsForCurrentRide);

        PassengerPayment payment = getPassengerPayment().build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyNewPaymentParametersCombination(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyNewPaymentParametersCombinationWithDifferentDriverForCurrentRide() {
        PassengerPayment currentRidePayment = getPassengerPayment()
                .withDriverId(ANOTHER_DRIVER_ID_UUID)
                .build();
        List<PassengerPayment> paymentsForCurrentRide = List.of(currentRidePayment);

        when(passengerPaymentRepository.findByRideId(any(UUID.class)))
                .thenReturn(paymentsForCurrentRide);

        PassengerPayment payment = getPassengerPayment().build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyNewPaymentParametersCombination(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyNewPaymentParametersCombinationWithDifferentPassengerAndDriverForCurrentRide() {
        PassengerPayment currentRidePayment = getPassengerPayment()
                .withPassengerId(ANOTHER_PASSENGER_ID_UUID)
                .withDriverId(ANOTHER_DRIVER_ID_UUID)
                .build();
        List<PassengerPayment> paymentsForCurrentRide = List.of(currentRidePayment);

        when(passengerPaymentRepository.findByRideId(any(UUID.class)))
                .thenReturn(paymentsForCurrentRide);

        PassengerPayment payment = getPassengerPayment().build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyNewPaymentParametersCombination(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowNoException_whenVerifyIfSameSuccessfulPaymentExistsWithNoExistingSuccessfulPayment() {
        when(passengerPaymentRepository.existsSuccessfulPayment(any(UUID.class), any(UUID.class), any(UUID.class)))
                .thenReturn(false);

        PassengerPayment payment = getPassengerPayment().build();

        assertThatNoException()
                .isThrownBy(() -> passengerPaymentVerifier.verifyIfSameSuccessfulPaymentExists(payment));
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyIfSameSuccessfulPaymentExistsWithExistingSuccessfulPayment() {
        when(passengerPaymentRepository.existsSuccessfulPayment(any(UUID.class), any(UUID.class), any(UUID.class)))
                .thenReturn(true);

        PassengerPayment payment = getPassengerPayment().build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyIfSameSuccessfulPaymentExists(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowNoException_whenVerifyPassengerWithCashPaymentMethod() {
        PassengerResponseDto passenger = getPassengerResponseDto().build();

        when(passengerClient.getPassengerById(anyString()))
                .thenReturn(passenger);

        PassengerPayment payment = getPassengerPayment()
                .withPaymentMethod(PaymentMethodEnum.CASH)
                .build();

        assertThatNoException()
                .isThrownBy(() -> passengerPaymentVerifier.verifyPassenger(payment));
    }

    @Test
    void shouldThrowNoException_whenVerifyPassengerWithBankCardPaymentMethodAndSamePassengerCardNumber() {
        String cardNumber = PASSENGER_CARD_NUMBER;
        PassengerResponseDto passenger = getPassengerResponseDto()
                .withCardNumber(cardNumber)
                .build();

        when(passengerClient.getPassengerById(anyString()))
                .thenReturn(passenger);

        PassengerPayment payment = getPassengerPayment()
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD)
                .withCardNumber(cardNumber)
                .build();

        assertThatNoException()
                .isThrownBy(() -> passengerPaymentVerifier.verifyPassenger(payment));
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyPassengerWithBankCardPaymentMethodAndDifferentPassengerCardNumber() {
        PassengerResponseDto passenger = getPassengerResponseDto()
                .withCardNumber(PASSENGER_CARD_NUMBER)
                .build();

        when(passengerClient.getPassengerById(anyString()))
                .thenReturn(passenger);

        PassengerPayment payment = getPassengerPayment()
                .withPaymentMethod(PaymentMethodEnum.BANK_CARD)
                .withCardNumber(DRIVER_CARD_NUMBER)
                .build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyPassenger(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowNoException_whenVerifyRideWithMatchForPassengerAndDriverAndAmount() {
        RideResponseDto ride = getEndedRideResponseDto().build();

        when(rideClient.getRideById(anyString()))
                .thenReturn(ride);

        PassengerPayment payment = getPassengerPayment().build();

        assertThatNoException()
                .isThrownBy(() -> passengerPaymentVerifier.verifyRide(payment));
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyRideWithNoMatchForPassengerAndDriverAndAmount() {
        RideResponseDto ride = getEndedRideResponseDto().build();

        when(rideClient.getRideById(anyString()))
                .thenReturn(ride);

        PassengerPayment payment = getPassengerPayment()
                .withPassengerId(ANOTHER_PASSENGER_ID_UUID)
                .withDriverId(ANOTHER_DRIVER_ID_UUID)
                .withAmount(DRIVER_PAYMENT_AMOUNT)
                .build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyRide(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyRideWithNoMatchForPassenger() {
        RideResponseDto ride = getEndedRideResponseDto().build();

        when(rideClient.getRideById(anyString()))
                .thenReturn(ride);

        PassengerPayment payment = getPassengerPayment()
                .withPassengerId(ANOTHER_PASSENGER_ID_UUID)
                .build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyRide(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyRideWithNoMatchForDriver() {
        RideResponseDto ride = getEndedRideResponseDto().build();

        when(rideClient.getRideById(anyString()))
                .thenReturn(ride);

        PassengerPayment payment = getPassengerPayment()
                .withDriverId(ANOTHER_DRIVER_ID_UUID)
                .build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyRide(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyRideWithNoMatchForAmount() {
        RideResponseDto ride = getEndedRideResponseDto().build();

        when(rideClient.getRideById(anyString()))
                .thenReturn(ride);

        PassengerPayment payment = getPassengerPayment()
                .withAmount(DRIVER_PAYMENT_AMOUNT)
                .build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyRide(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyRideWithNoMatchForPassengerAndAmount() {
        RideResponseDto ride = getEndedRideResponseDto().build();

        when(rideClient.getRideById(anyString()))
                .thenReturn(ride);

        PassengerPayment payment = getPassengerPayment()
                .withPassengerId(ANOTHER_PASSENGER_ID_UUID)
                .withAmount(DRIVER_PAYMENT_AMOUNT)
                .build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyRide(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyRideWithNoMatchForPassengerAndDriver() {
        RideResponseDto ride = getEndedRideResponseDto().build();

        when(rideClient.getRideById(anyString()))
                .thenReturn(ride);

        PassengerPayment payment = getPassengerPayment()
                .withPassengerId(ANOTHER_PASSENGER_ID_UUID)
                .withDriverId(ANOTHER_DRIVER_ID_UUID)
                .build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyRide(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyRideWithNoMatchForDriverAndAmount() {
        RideResponseDto ride = getEndedRideResponseDto().build();

        when(rideClient.getRideById(anyString()))
                .thenReturn(ride);

        PassengerPayment payment = getPassengerPayment()
                .withDriverId(ANOTHER_DRIVER_ID_UUID)
                .withAmount(DRIVER_PAYMENT_AMOUNT)
                .build();

        assertThatThrownBy(() -> passengerPaymentVerifier.verifyRide(payment))
                .isInstanceOf(IllegalStateException.class);
    }
}
