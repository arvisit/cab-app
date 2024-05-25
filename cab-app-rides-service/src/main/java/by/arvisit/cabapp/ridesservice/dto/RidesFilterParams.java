package by.arvisit.cabapp.ridesservice.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

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
        ZonedDateTime bookRide,
        ZonedDateTime cancelRide,
        ZonedDateTime acceptRide,
        ZonedDateTime beginRide,
        ZonedDateTime endRide,
        ZonedDateTime finishRide) {

}
