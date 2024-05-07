package by.arvisit.cabapp.driverservice.dto;

import static by.arvisit.cabapp.common.util.ValidationRegexp.CARD_NUMBER;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record DriverRequestDto(
        @NotBlank(message = "{by.arvisit.cabapp.driverservice.dto.DriverRequestDto.name.NotBlank.message}")
        @Size(max = 100, message = "{by.arvisit.cabapp.driverservice.dto.DriverRequestDto.name.Size.message}")
        String name,
        @NotBlank(message = "{by.arvisit.cabapp.driverservice.dto.DriverRequestDto.email.NotBlank.message}")
        @Email(message = "{by.arvisit.cabapp.driverservice.dto.DriverRequestDto.email.Email.message}")
        String email,
        @Pattern(regexp = CARD_NUMBER,
            message = "{by.arvisit.cabapp.driverservice.dto.DriverRequestDto.cardNumber.Pattern.message}")
        String cardNumber,
        @NotNull(message = "{by.arvisit.cabapp.driverservice.dto.DriverRequestDto.car.NotNull.message}")
        @Valid
        CarRequestDto car) {

}
