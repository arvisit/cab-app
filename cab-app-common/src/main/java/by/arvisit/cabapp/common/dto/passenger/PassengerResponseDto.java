package by.arvisit.cabapp.common.dto.passenger;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record PassengerResponseDto(
        String id,
        String name,
        String email,
        String cardNumber) {

}
