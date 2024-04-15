package by.arvisit.cabapp.common.dto.driver;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record CarResponseDto(
        String id,
        CarManufacturerResponseDto manufacturer,
        ColorResponseDto color,
        String registrationNumber) {

}
