package by.arvisit.cabapp.common.dto.driver;

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
