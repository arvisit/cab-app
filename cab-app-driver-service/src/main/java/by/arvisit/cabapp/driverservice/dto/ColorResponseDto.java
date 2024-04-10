package by.arvisit.cabapp.driverservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record ColorResponseDto(
        Integer id,
        String name) {

}
