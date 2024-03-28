package by.arvisit.cabapp.passengerservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record PassengerResponseDto(String id, String name, String email, String cardNumber) {

}
