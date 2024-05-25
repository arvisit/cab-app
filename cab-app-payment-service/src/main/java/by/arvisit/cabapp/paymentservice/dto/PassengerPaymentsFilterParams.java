package by.arvisit.cabapp.paymentservice.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record PassengerPaymentsFilterParams(
        Integer page,
        Integer size,
        String sort,
        String status,
        String paymentMethod,
        UUID passengerId,
        UUID driverId,
        UUID rideId,
        ZonedDateTime timestamp) {

}
