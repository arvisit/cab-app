package by.arvisit.cabapp.ridesservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record CarManufacturerResponseDto(
        Integer id,
        String name) {

}
