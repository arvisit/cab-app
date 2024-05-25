package by.arvisit.cabapp.paymentservice.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record DriverPaymentsFilterParams(
        Integer page,
        Integer size,
        String sort,
        String status,
        String operation,
        UUID driverId,
        ZonedDateTime timestamp) {

}
