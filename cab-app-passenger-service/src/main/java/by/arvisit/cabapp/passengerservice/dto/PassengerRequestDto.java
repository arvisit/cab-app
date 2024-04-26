package by.arvisit.cabapp.passengerservice.dto;

import static by.arvisit.cabapp.common.util.ValidationRegexp.CARD_NUMBER;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record PassengerRequestDto(
        @NotBlank(message = "{by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto.name.NotBlank.message}")
        @Size(max = 100, message = "{by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto.name.Size.message}")
        String name,
        @NotBlank(message = "{by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto.email.NotBlank.message}")
        @Size(max = 100, message = "{by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto.email.Size.message}")
        @Email(message = "{by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto.email.Email.message}")
        String email,
        @Pattern(regexp = CARD_NUMBER,
                message = "{by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto.cardNumber.Pattern.message}")
        String cardNumber) {

 }
