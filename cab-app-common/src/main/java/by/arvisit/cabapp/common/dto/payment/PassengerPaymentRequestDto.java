package by.arvisit.cabapp.common.dto.payment;

import java.math.BigDecimal;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record PassengerPaymentRequestDto(
        String rideId,
        String passengerId,
        String driverId,
        BigDecimal amount,
        String paymentMethod,
        String cardNumber) {
    
}
