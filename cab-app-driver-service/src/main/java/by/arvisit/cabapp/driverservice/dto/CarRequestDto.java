package by.arvisit.cabapp.driverservice.dto;

import static by.arvisit.cabapp.common.util.ValidationRegexp.CAR_REGISTRATION_NUMBER_BY;

import by.arvisit.cabapp.driverservice.validation.IsCarManufacturerExistById;
import by.arvisit.cabapp.driverservice.validation.IsColorExistById;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record CarRequestDto(
        @NotNull(message = "{by.arvisit.cabapp.driverservice.dto.CarRequestDto.manufacturerId.NotNull.message}")
        @IsCarManufacturerExistById
        Integer manufacturerId,
        @NotNull(message = "{by.arvisit.cabapp.driverservice.dto.CarRequestDto.colorId.NotNull.message}")
        @IsColorExistById
        Integer colorId,
        @Pattern(regexp = CAR_REGISTRATION_NUMBER_BY,
            message = "{by.arvisit.cabapp.driverservice.dto.CarRequestDto.registrationNumber.Pattern.message}")
        String registrationNumber) {

}
