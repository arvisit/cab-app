package by.arvisit.cabapp.driverservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record CarResponseDto(
        String id,
        CarManufacturerResponseDto manufacturer,
        ColorResponseDto color,
        String registrationNumber) {

}
