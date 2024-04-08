package by.arvisit.cabapp.ridesservice.dto;

import java.time.ZonedDateTime;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record RideResponseDto(
        String id,
        String initialCost,
        String finalCost,
        String passengerId,
        String driverId,
        String promoCode,
        String status,
        String paymentMethod,
        Boolean isPaid,
        String startAddress,
        String destinationAddress,
        Integer passengerScore,
        Integer driverScore,
        ZonedDateTime bookRide,
        ZonedDateTime cancelRide,
        ZonedDateTime acceptRide,
        ZonedDateTime beginRide,
        ZonedDateTime endRide,
        ZonedDateTime finishRide) {

}
