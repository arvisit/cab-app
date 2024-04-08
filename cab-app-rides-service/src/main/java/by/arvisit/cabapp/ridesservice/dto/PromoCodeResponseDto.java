package by.arvisit.cabapp.ridesservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record PromoCodeResponseDto(
        Long id,
        String keyword,
        Integer discountPercent,
        Boolean isActive) {

}
