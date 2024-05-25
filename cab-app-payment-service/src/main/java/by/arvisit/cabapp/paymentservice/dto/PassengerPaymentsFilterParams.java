package by.arvisit.cabapp.paymentservice.dto;

import java.util.UUID;

import by.arvisit.cabapp.common.util.DateRange;
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
        DateRange timestamp) {

}
