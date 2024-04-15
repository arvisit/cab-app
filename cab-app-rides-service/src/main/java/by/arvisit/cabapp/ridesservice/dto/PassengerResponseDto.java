package by.arvisit.cabapp.ridesservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record PassengerResponseDto(
        String id,
        String name,
        String email,
        String cardNumber) {

}
