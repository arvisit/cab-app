package by.arvisit.cabapp.ridesservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record ColorResponseDto(
        Integer id,
        String name) {

}
