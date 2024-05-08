package by.arvisit.cabapp.common.dto.driver;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record ColorResponseDto(
        Integer id,
        String name) {

}
