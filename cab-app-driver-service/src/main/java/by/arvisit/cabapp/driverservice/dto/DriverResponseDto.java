package by.arvisit.cabapp.driverservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record DriverResponseDto(
        String id,
        String name,
        String email,
        String cardNumber,
        CarResponseDto car,
        Boolean isAvailable) {

}
