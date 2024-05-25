package by.arvisit.cabapp.paymentservice.dto;

import java.util.UUID;

import by.arvisit.cabapp.common.util.DateRange;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record DriverPaymentsFilterParams(
        Integer page,
        Integer size,
        String sort,
        String status,
        String operation,
        UUID driverId,
        DateRange timestamp) {

}
