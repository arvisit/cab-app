package by.arvisit.cabapp.paymentservice.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.paymentservice.client.PassengerClient;
import by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum;
import by.arvisit.cabapp.paymentservice.persistence.repository.PassengerPaymentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class PassengerPaymentVerifier {

    private static final String RIDE_PASSENGER_DRIVER_ILLEGAL_COMBINATION_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment.rideId.passengerId.driverId.IllegalStateException.template";
    private static final String RIDE_PAYMENT_COMPLITED_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment.rideId.passengerId.driverId.status.IllegalStateException.template";
    private static final String CARD_NUMBER_MISMATCH_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment.cardNumber.IllegalStateException.template";

    private final PassengerPaymentRepository passengerPaymentRepository;
    private final MessageSource messageSource;
    private final PassengerClient passengerClient;

    public void verifyNewPayment(PassengerPayment newPayment) {
        verifyPassenger(newPayment);
        verifyNewPaymentParametersCombination(newPayment);
        verifyIfSameSuccessfulPaymentExists(newPayment);
    }

    public void verifyNewPaymentParametersCombination(PassengerPayment newPayment) {
        List<PassengerPayment> paymentsForCurrentRide = passengerPaymentRepository
                .findByRideId(newPayment.getRideId());

        UUID newPaymentDriverId = newPayment.getDriverId();
        UUID newPaymentPassengerId = newPayment.getPassengerId();

        if (!paymentsForCurrentRide.isEmpty() && paymentsForCurrentRide.stream()
                .noneMatch(p -> p.getDriverId().equals(newPaymentDriverId)
                        && p.getPassengerId().equals(newPaymentPassengerId))) {

            String coflictWithExistingPaymentDetailsErrorMessage = messageSource.getMessage(
                    RIDE_PASSENGER_DRIVER_ILLEGAL_COMBINATION_MESSAGE_TEMPLATE_KEY,
                    new Object[] { newPayment.getRideId() }, null);
            throw new IllegalStateException(coflictWithExistingPaymentDetailsErrorMessage);
        }
    }

    public void verifyIfSameSuccessfulPaymentExists(PassengerPayment newPayment) {
        UUID newPaymentDriverId = newPayment.getDriverId();
        UUID newPaymentPassengerId = newPayment.getPassengerId();

        if (passengerPaymentRepository.existsSuccessfulPayment(newPayment.getRideId(),
                newPaymentPassengerId, newPaymentDriverId)) {
            String existingSuccessfulPaymentErrorMessage = messageSource.getMessage(
                    RIDE_PAYMENT_COMPLITED_MESSAGE_TEMPLATE_KEY,
                    new Object[] { newPayment.getRideId() }, null);
            throw new IllegalStateException(existingSuccessfulPaymentErrorMessage);
        }

    }

    public void verifyPassenger(PassengerPayment newPayment) {
        PassengerResponseDto passenger = passengerClient.getPassengerById(newPayment.getPassengerId().toString());

        if (newPayment.getPaymentMethod() == PaymentMethodEnum.BANK_CARD
                && !newPayment.getCardNumber().equals(passenger.cardNumber())) {

            String errorMessage = messageSource.getMessage(
                    CARD_NUMBER_MISMATCH_MESSAGE_TEMPLATE_KEY,
                    new Object[] {}, null);
            throw new IllegalStateException(errorMessage);
        }
    }
}
