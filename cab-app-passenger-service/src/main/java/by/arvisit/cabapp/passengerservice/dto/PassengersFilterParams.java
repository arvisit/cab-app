package by.arvisit.cabapp.passengerservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record PassengersFilterParams(
        Integer page,
        Integer size,
        String sort,
        String name,
        String email) {

}
