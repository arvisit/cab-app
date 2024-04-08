package by.arvisit.cabapp.ridesservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record PromoCodeRequestDto(
        @NotBlank(message = "{by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto.keyword.NotBlank.message}")
        @Size(max = 20, message = "{by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto.keyword.Size.message}")
        String keyword,
        @NotNull(message = "{by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto.discount.NotNull.message}")
        @Max(value = 100, message = "{by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto.discount.Max.message}")
        @PositiveOrZero(message = "{by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto.discount.PositiveOrZero.message}")
        Integer discountPercent) {

}
