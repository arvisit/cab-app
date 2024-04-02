package by.arvisit.cabapp.driverservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record CarManufacturerResponseDto(
        Integer id,
        String name) {

}
