package by.arvisit.cabapp.paymentservice.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record PassengerPaymentResponseDto(
        String id,
        String rideId,
        String passengerId,
        String driverId,
        BigDecimal amount,
        BigDecimal feeAmount,
        String paymentMethod,
        String status,
        String cardNumber,
        ZonedDateTime timestamp) {

}
