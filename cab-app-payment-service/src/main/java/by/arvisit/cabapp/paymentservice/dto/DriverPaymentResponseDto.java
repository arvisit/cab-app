package by.arvisit.cabapp.paymentservice.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record DriverPaymentResponseDto(
        String id,
        String driverId,
        BigDecimal amount,
        String operation,
        String cardNumber,
        String status,
        ZonedDateTime timestamp) {

}
