package by.arvisit.cabapp.paymentservice.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record DriverAccountBalanceResponseDto(
        String driverId,
        BigDecimal balance) {

}
