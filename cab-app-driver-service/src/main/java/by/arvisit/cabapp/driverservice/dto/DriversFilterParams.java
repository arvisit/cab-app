package by.arvisit.cabapp.driverservice.dto;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record DriversFilterParams(
        Integer page,
        Integer size,
        String sort,
        String name,
        String email,
        Boolean isAvailable,
        String carManufacturerName) {

}
