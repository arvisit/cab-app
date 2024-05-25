package by.arvisit.cabapp.paymentservice.mapper;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.util.DateRange;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentsFilterParams;

@Component
public class DriverPaymentsFilterParamsMapper {

    private static final String TIMESTAMP = "timestamp";
    private static final String DRIVER_ID = "driverId";
    private static final String STATUS = "status";
    private static final String OPERATION = "operation";

    public DriverPaymentsFilterParams fromMapParams(Map<String, String> params) {
        String timestamp = params.get(TIMESTAMP);
        String driverId = params.get(DRIVER_ID);
        return DriverPaymentsFilterParams.builder()
                .withStatus(params.get(STATUS))
                .withOperation(params.get(OPERATION))
                .withDriverId(driverId != null
                        ? UUID.fromString(driverId)
                        : null)
                .withTimestamp(timestamp != null
                        ? DateRange.fromSingleValue(timestamp)
                        : null)
                .build();
    }
}
