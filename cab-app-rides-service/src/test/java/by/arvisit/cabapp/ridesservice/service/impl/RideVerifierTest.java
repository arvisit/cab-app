package by.arvisit.cabapp.ridesservice.service.impl;

import static by.arvisit.cabapp.ridesservice.util.RideTestData.ANOTHER_DRIVER_ID_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_DRIVER_ID_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getAcceptedRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getBeganRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getBookedRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getCanceledRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getEndedRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getFinishedRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getFinishedWithScoresRide;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.ridesservice.client.DriverClient;
import by.arvisit.cabapp.ridesservice.client.PassengerClient;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import by.arvisit.cabapp.ridesservice.persistence.repository.RideRepository;
import by.arvisit.cabapp.ridesservice.util.RideTestData;

@ExtendWith(MockitoExtension.class)
class RideVerifierTest {

    @InjectMocks
    private RideVerifier rideVerifier;
    @Mock
    private RideRepository rideRepository;
    @Mock
    private MessageSource messageSource;
    @Mock
    private PassengerClient passengerClient;
    @Mock
    private DriverClient driverClient;

    @Test
    void shouldThrowNoException_whenVerifyCreateRideWithExistingPassenger() {
        Ride ride = RideTestData.getRideToSave().build();
        PassengerResponseDto passenger = RideTestData.getPassengerResponseDto().build();
        when(passengerClient.getPassengerById(anyString()))
                .thenReturn(passenger);

        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyCreateRide(ride));
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyCreateRideWithPassengerHasInProgressRide() {
        Ride ride = RideTestData.getRideToSave().build();
        PassengerResponseDto passenger = RideTestData.getPassengerResponseDto().build();
        when(passengerClient.getPassengerById(anyString()))
                .thenReturn(passenger);
        when(rideRepository.hasInProgressRides(any(UUID.class)))
                .thenReturn(true);

        assertThatThrownBy(() -> rideVerifier.verifyCreateRide(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("bookedAndCanceled")
    void shouldThrowNoException_whenVerifyCancelRideWithValidStatus(Ride ride) {
        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyCancelRide(ride));
    }

    @ParameterizedTest
    @MethodSource("beganAndEndedAndFinished")
    void shouldThrowIllegalStateException_whenVerifyCancelRideWithInvalidStatus(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyCancelRide(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowNoException_whenVerifyAcceptRideForBookedRideAndAvailableDriver() {
        Ride ride = getBookedRide().build();

        DriverResponseDto driver = RideTestData.getDriverResponseDto().build();

        when(driverClient.getDriverById(anyString()))
                .thenReturn(driver);

        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyAcceptRide(ride, RIDE_DEFAULT_DRIVER_ID_STRING));
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyAcceptRideForBookedRideAndNotAvailableDriver() {
        Ride ride = getBookedRide().build();

        DriverResponseDto driver = RideTestData.getDriverResponseDto()
                .withIsAvailable(false)
                .build();

        when(driverClient.getDriverById(anyString()))
                .thenReturn(driver);

        assertThatThrownBy(() -> rideVerifier.verifyAcceptRide(ride, RIDE_DEFAULT_DRIVER_ID_STRING))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowNoException_whenVerifyAcceptRideForAcceptedRideAndSameDriver() {
        Ride ride = getAcceptedRide().build();

        DriverResponseDto driver = RideTestData.getDriverResponseDto().build();

        when(driverClient.getDriverById(anyString()))
                .thenReturn(driver);

        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyAcceptRide(ride, RIDE_DEFAULT_DRIVER_ID_STRING));
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyAcceptRideForAcceptedRideAndAnotherDriver() {
        Ride ride = getAcceptedRide().build();

        assertThatThrownBy(() -> rideVerifier.verifyAcceptRide(ride, ANOTHER_DRIVER_ID_STRING))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("canceledAndBeganAndEndedAndFinished")
    void shouldThrowIllegalStateException_whenVerifyAcceptRideWithInvalidStatus(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyAcceptRide(ride, RIDE_DEFAULT_DRIVER_ID_STRING))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("acceptedAndBegan")
    void shouldThrowNoException_whenVerifyBeginRideWithValidStatus(Ride ride) {
        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyBeginRide(ride));
    }

    @ParameterizedTest
    @MethodSource("bookedAndCanceledAndEndedAndFinished")
    void shouldThrowIllegalStateException_whenVerifyBeginRideWithInvalidStatus(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyBeginRide(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("beganAndEnded")
    void shouldThrowNoException_whenVerifyEndRideWithValidStatus(Ride ride) {
        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyEndRide(ride));
    }

    @ParameterizedTest
    @MethodSource("bookedAndAcceptedAndCanceledAndFinished")
    void shouldThrowIllegalStateException_whenVerifyEndRideWithInvalidStatus(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyEndRide(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("endedAndFinishedPaid")
    void shouldThrowNoException_whenVerifyFinishRideWithValidStatePaid(Ride ride) {
        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyFinishRide(ride));
    }

    @Test
    void shouldThrowNoException_whenVerifyFinishRideWithFinishedNotPaid() {
        Ride ride = getFinishedRide().withIsPaid(false).build();

        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyFinishRide(ride));
    }

    @ParameterizedTest
    @MethodSource("bookedAndCanceledAndAcceptedAndBeganAndEndedNotPaid")
    void shouldThrowIllegalStateException_whenVerifyFinishRideWithInvalidStateNotPaid(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyFinishRide(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("bookedAndCanceledAndAcceptedAndBeganPaid")
    void shouldThrowIllegalStateException_whenVerifyFinishRideWithInvalidStatePaid(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyFinishRide(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("endedAndFinished")
    void shouldThrowNoException_whenVerifyConfirmPaymentWithValidStatus(Ride ride) {
        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyConfirmPayment(ride));
    }

    @ParameterizedTest
    @MethodSource("bookedAndCanceledAndAcceptedAndBegan")
    void shouldThrowIllegalStateException_whenVerifyConfirmPaymentWithInvalidStatus(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyConfirmPayment(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("bookedAndAcceptedAndBeganAndEndedNotPaid")
    void shouldThrowNoException_whenVerifyApplyPromoCodeWithValidState(Ride ride) {
        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyApplyPromoCode(ride));
    }

    @ParameterizedTest
    @MethodSource("canceledAndFinished")
    void shouldThrowIllegalStateException_whenVerifyApplyPromoCodeWithInvalidStatus(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyApplyPromoCode(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("bookedAndCanceledAndAcceptedAndBeganAndEndedAndFinishedPaid")
    void shouldThrowIllegalStateException_whenVerifyApplyPromoCodeWithInvalidStatePaid(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyApplyPromoCode(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("bookedAndAcceptedAndBeganAndEndedNotPaid")
    void shouldThrowNoException_whenVerifyChangePaymentMethodWithValidState(Ride ride) {
        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyChangePaymentMethod(ride));
    }

    @ParameterizedTest
    @MethodSource("canceledAndFinished")
    void shouldThrowIllegalStateException_whenVerifyChangePaymentMethodWithInvalidStatus(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyChangePaymentMethod(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("bookedAndCanceledAndAcceptedAndBeganAndEndedAndFinishedPaid")
    void shouldThrowIllegalStateException_whenVerifyChangePaymentMethodWithInvalidStatePaid(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyChangePaymentMethod(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowNoException_whenVerifyScoreDriverWithFinishedNotScored() {
        Ride ride = getFinishedRide().build();

        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyScoreDriver(ride));
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyScoreDriverWithFinishedScored() {
        Ride ride = getFinishedWithScoresRide().build();

        assertThatThrownBy(() -> rideVerifier.verifyScoreDriver(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("bookedAndCanceledAndAcceptedAndBeganAndEnded")
    void shouldThrowIllegalStateException_whenVerifyScoreDriverWithInvalidStatus(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyScoreDriver(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowNoException_whenVerifyScorePassengerWithFinishedNotScored() {
        Ride ride = getFinishedRide().build();

        assertThatNoException()
                .isThrownBy(() -> rideVerifier.verifyScorePassenger(ride));
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyScorePassengerWithFinishedScored() {
        Ride ride = getFinishedWithScoresRide().build();

        assertThatThrownBy(() -> rideVerifier.verifyScorePassenger(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("bookedAndCanceledAndAcceptedAndBeganAndEnded")
    void shouldThrowIllegalStateException_whenVerifyScorePassengerWithInvalidStatus(Ride ride) {
        assertThatThrownBy(() -> rideVerifier.verifyScorePassenger(ride))
                .isInstanceOf(IllegalStateException.class);
    }

    private static Stream<Ride> canceledAndFinished() {
        return Stream.of(getCanceledRide().build(), getFinishedRide().build());
    }

    private static Stream<Ride> bookedAndCanceled() {
        return Stream.of(getBookedRide().build(), getCanceledRide().build());
    }

    private static Stream<Ride> acceptedAndBegan() {
        return Stream.of(getAcceptedRide().build(), getBeganRide().build());
    }

    private static Stream<Ride> beganAndEnded() {
        return Stream.of(getBeganRide().build(), getEndedRide().build());
    }

    private static Stream<Ride> endedAndFinished() {
        return Stream.of(getEndedRide().build(), getFinishedRide().build());
    }

    private static Stream<Ride> endedAndFinishedPaid() {
        return Stream.of(getEndedRide().withIsPaid(true).build(), getFinishedRide().withIsPaid(true).build());
    }

    private static Stream<Ride> bookedAndAcceptedAndBeganAndEndedNotPaid() {
        return Stream.of(getBookedRide().withIsPaid(false).build(), getAcceptedRide().withIsPaid(false).build(),
                getBeganRide().withIsPaid(false).build(), getEndedRide().withIsPaid(false).build());
    }

    private static Stream<Ride> bookedAndCanceledAndAcceptedAndBeganAndEndedAndFinishedPaid() {
        return Stream.of(getBookedRide().withIsPaid(true).build(), getCanceledRide().withIsPaid(true).build(),
                getAcceptedRide().withIsPaid(true).build(), getBeganRide().withIsPaid(true).build(),
                getEndedRide().withIsPaid(true).build(), getFinishedRide().withIsPaid(true).build());
    }

    private static Stream<Ride> bookedAndCanceledAndAcceptedAndBeganAndEndedNotPaid() {
        return Stream.of(getBookedRide().withIsPaid(false).build(), getCanceledRide().withIsPaid(false).build(),
                getAcceptedRide().withIsPaid(false).build(), getBeganRide().withIsPaid(false).build(),
                getEndedRide().withIsPaid(false).build());
    }

    private static Stream<Ride> bookedAndCanceledAndAcceptedAndBeganPaid() {
        return Stream.of(getBookedRide().withIsPaid(true).build(), getCanceledRide().withIsPaid(true).build(),
                getAcceptedRide().withIsPaid(true).build(), getBeganRide().withIsPaid(true).build());
    }

    private static Stream<Ride> bookedAndCanceledAndAcceptedAndBegan() {
        return Stream.of(getBookedRide().build(), getCanceledRide().build(),
                getAcceptedRide().build(), getBeganRide().build());
    }

    private static Stream<Ride> beganAndEndedAndFinished() {
        return Stream.of(getBeganRide().build(), getEndedRide().build(), getFinishedRide().build());
    }

    private static Stream<Ride> bookedAndAcceptedAndCanceledAndFinished() {
        return Stream.of(getBookedRide().build(), getAcceptedRide().build(), getCanceledRide().build(),
                getFinishedRide().build());
    }

    private static Stream<Ride> bookedAndCanceledAndEndedAndFinished() {
        return Stream.of(getBookedRide().build(), getCanceledRide().build(), getEndedRide().build(),
                getFinishedRide().build());
    }

    private static Stream<Ride> canceledAndBeganAndEndedAndFinished() {
        return Stream.of(getCanceledRide().build(), getBeganRide().build(), getEndedRide().build(),
                getFinishedRide().build());
    }

    private static Stream<Ride> bookedAndCanceledAndAcceptedAndBeganAndEnded() {
        return Stream.of(getBookedRide().build(), getCanceledRide().build(), getAcceptedRide().build(),
                getBeganRide().build(), getEndedRide().build());
    }
}
