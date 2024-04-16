package by.arvisit.cabapp.ridesservice.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record RideResponseDto(
        String id,
        BigDecimal initialCost,
        BigDecimal finalCost,
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
