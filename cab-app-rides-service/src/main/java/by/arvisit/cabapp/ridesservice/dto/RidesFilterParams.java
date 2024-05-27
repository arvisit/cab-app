package by.arvisit.cabapp.ridesservice.dto;

import java.util.UUID;

import by.arvisit.cabapp.common.util.DateRange;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record RidesFilterParams(
        Integer page,
        Integer size,
        String sort,
        String startAddress,
        String destinationAddress,
        String status,
        String paymentMethod,
        UUID passengerId,
        UUID driverId,
        DateRange bookRide,
        DateRange cancelRide,
        DateRange acceptRide,
        DateRange beginRide,
        DateRange endRide,
        DateRange finishRide) {

}
