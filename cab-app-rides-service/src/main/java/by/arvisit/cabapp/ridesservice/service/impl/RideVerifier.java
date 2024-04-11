package by.arvisit.cabapp.ridesservice.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import by.arvisit.cabapp.ridesservice.persistence.model.RideStatusEnum;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class RideVerifier {

    private static final String STATUS_COULD_NOT_BE_CHANGED_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.status.IllegalStateException.template";
    private static final String RIDE_ALREADY_ACCEPTED_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.driverId.IllegalStateException.template";
    private static final String ILLEGAL_STATUS_FOR_PAYMENT_CONFIRMATION_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.paid.IllegalStateException.template";
    private static final String ILLEGAL_STATUS_TO_APPLY_PROMO_CODE_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.promoCode.IllegalStateException.template";
    private static final String ILLEGAL_STATUS_TO_CHANGE_PAYMENT_METHOD_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.paymentMethod.IllegalStateException.template";
    private static final String ILLEGAL_STATUS_TO_SCORE_DRIVER_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.driverScore.IllegalStateException.template";
    private static final String ILLEGAL_STATUS_TO_SCORE_PASSENGER_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.passengerScore.IllegalStateException.template";

    private final MessageSource messageSource;

    public void verifyCancelRide(Ride ride) {
        RideStatusEnum currentStatus = ride.getStatus();
        RideStatusEnum newStatus = RideStatusEnum.CANCELED;

        if (currentStatus == RideStatusEnum.BEGIN_RIDE || currentStatus == RideStatusEnum.END_RIDE
                || currentStatus == RideStatusEnum.FINISHED) {
            throwExceptionForIllegalStatusChange(ride, newStatus);
        }
    }

    public void verifyAcceptRide(Ride ride, String driverId) {
        RideStatusEnum currentStatus = ride.getStatus();
        RideStatusEnum newStatus = RideStatusEnum.ACCEPTED;

        if (currentStatus == RideStatusEnum.CANCELED || currentStatus == RideStatusEnum.BEGIN_RIDE
                || currentStatus == RideStatusEnum.END_RIDE || currentStatus == RideStatusEnum.FINISHED) {
            throwExceptionForIllegalStatusChange(ride, newStatus);
        }

        if (currentStatus == RideStatusEnum.ACCEPTED && !driverId.equals(ride.getDriverId().toString())) {
            String errorMessage = messageSource.getMessage(
                    RIDE_ALREADY_ACCEPTED_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId(), driverId }, null);
            throw new IllegalStateException(errorMessage);
        }
    }

    public void verifyBeginRide(Ride ride) {
        RideStatusEnum currentStatus = ride.getStatus();
        RideStatusEnum newStatus = RideStatusEnum.BEGIN_RIDE;

        if (currentStatus != RideStatusEnum.ACCEPTED && currentStatus != RideStatusEnum.BEGIN_RIDE) {
            throwExceptionForIllegalStatusChange(ride, newStatus);
        }
    }

    public void verifyEndRide(Ride ride) {
        RideStatusEnum currentStatus = ride.getStatus();
        RideStatusEnum newStatus = RideStatusEnum.END_RIDE;

        if (currentStatus != RideStatusEnum.BEGIN_RIDE && currentStatus != RideStatusEnum.END_RIDE) {
            throwExceptionForIllegalStatusChange(ride, newStatus);
        }
    }

    public void verifyFinishRide(Ride ride) {
        RideStatusEnum currentStatus = ride.getStatus();
        RideStatusEnum newStatus = RideStatusEnum.FINISHED;

        if (currentStatus != RideStatusEnum.END_RIDE && currentStatus != RideStatusEnum.FINISHED && !ride.getIsPaid()) {
            throwExceptionForIllegalStatusChange(ride, newStatus);
        }
    }

    public void verifyConfirmPayment(Ride ride) {
        RideStatusEnum currentStatus = ride.getStatus();

        if (currentStatus != RideStatusEnum.END_RIDE && currentStatus != RideStatusEnum.FINISHED) {
            String errorMessage = messageSource.getMessage(
                    ILLEGAL_STATUS_FOR_PAYMENT_CONFIRMATION_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalStateException(errorMessage);
        }
    }

    public void verifyApplyPromoCode(Ride ride) {
        RideStatusEnum currentStatus = ride.getStatus();

        if (currentStatus == RideStatusEnum.CANCELED || currentStatus == RideStatusEnum.FINISHED || ride.getIsPaid()) {
            String errorMessage = messageSource.getMessage(
                    ILLEGAL_STATUS_TO_APPLY_PROMO_CODE_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalStateException(errorMessage);
        }
    }

    public void verifyChangePaymentMethod(Ride ride) {
        RideStatusEnum currentStatus = ride.getStatus();

        if (currentStatus == RideStatusEnum.CANCELED || currentStatus == RideStatusEnum.FINISHED || ride.getIsPaid()) {
            String errorMessage = messageSource.getMessage(
                    ILLEGAL_STATUS_TO_CHANGE_PAYMENT_METHOD_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalStateException(errorMessage);
        }
    }

    public void verifyScoreDriver(Ride ride) {
        RideStatusEnum currentStatus = ride.getStatus();

        if (currentStatus != RideStatusEnum.FINISHED || ride.getDriverScore() != null) {
            String errorMessage = messageSource.getMessage(
                    ILLEGAL_STATUS_TO_SCORE_DRIVER_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalStateException(errorMessage);
        }
    }

    public void verifyScorePassenger(Ride ride) {
        RideStatusEnum currentStatus = ride.getStatus();

        if (currentStatus != RideStatusEnum.FINISHED || ride.getPassengerScore() != null) {
            String errorMessage = messageSource.getMessage(
                    ILLEGAL_STATUS_TO_SCORE_PASSENGER_MESSAGE_TEMPLATE_KEY,
                    new Object[] { ride.getId() }, null);
            throw new IllegalStateException(errorMessage);
        }
    }

    private void throwExceptionForIllegalStatusChange(Ride ride, RideStatusEnum newStatus) {
        String errorMessage = messageSource.getMessage(
                STATUS_COULD_NOT_BE_CHANGED_MESSAGE_TEMPLATE_KEY,
                new Object[] { ride.getId(), ride.getStatus(), newStatus }, null);
        throw new IllegalStateException(errorMessage);
    }
}
