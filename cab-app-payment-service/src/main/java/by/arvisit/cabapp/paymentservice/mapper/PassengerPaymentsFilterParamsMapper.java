package by.arvisit.cabapp.paymentservice.mapper;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.util.DateRange;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentsFilterParams;

@Component
public class PassengerPaymentsFilterParamsMapper {

    private static final String TIMESTAMP = "timestamp";
    private static final String PASSENGER_ID = "passengerId";
    private static final String DRIVER_ID = "driverId";
    private static final String RIDE_ID = "rideId";
    private static final String STATUS = "status";
    private static final String PAYMENT_METHOD = "paymentMethod";

    public PassengerPaymentsFilterParams fromMapParams(Map<String, String> params) {
        String timestamp = params.get(TIMESTAMP);
        String passengerId = params.get(PASSENGER_ID);
        String driverId = params.get(DRIVER_ID);
        String rideId = params.get(RIDE_ID);
        return PassengerPaymentsFilterParams.builder()
                .withStatus(params.get(STATUS))
                .withPaymentMethod(params.get(PAYMENT_METHOD))
                .withPassengerId(getNullOrUUID(passengerId))
                .withDriverId(getNullOrUUID(driverId))
                .withRideId(getNullOrUUID(rideId))
                .withTimestamp(timestamp != null
                        ? DateRange.fromSingleValue(timestamp)
                        : null)
                .build();
    }

    private UUID getNullOrUUID(String str) {
        return str != null
                ? UUID.fromString(str)
                : null;
    }
}
