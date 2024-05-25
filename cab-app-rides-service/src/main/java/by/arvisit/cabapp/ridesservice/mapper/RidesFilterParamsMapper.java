package by.arvisit.cabapp.ridesservice.mapper;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.util.DateRange;
import by.arvisit.cabapp.ridesservice.dto.RidesFilterParams;

@Component
public class RidesFilterParamsMapper {

    private static final String PAYMENT_METHOD = "paymentMethod";
    private static final String STATUS = "status";
    private static final String DESTINATION_ADDRESS = "destinationAddress";
    private static final String START_ADDRESS = "startAddress";
    private static final String FINISH_RIDE = "finishRide";
    private static final String END_RIDE = "endRide";
    private static final String BEGIN_RIDE = "beginRide";
    private static final String ACCEPT_RIDE = "acceptRide";
    private static final String CANCEL_RIDE = "cancelRide";
    private static final String BOOK_RIDE = "bookRide";
    private static final String DRIVER_ID = "driverId";
    private static final String PASSENGER_ID = "passengerId";

    public RidesFilterParams fromMapParams(Map<String, String> params) {
        String passengerId = params.get(PASSENGER_ID);
        String driverId = params.get(DRIVER_ID);
        String bookRide = params.get(BOOK_RIDE);
        String cancelRide = params.get(CANCEL_RIDE);
        String acceptRide = params.get(ACCEPT_RIDE);
        String beginRide = params.get(BEGIN_RIDE);
        String endRide = params.get(END_RIDE);
        String finishRide = params.get(FINISH_RIDE);
        return RidesFilterParams.builder()
                .withStartAddress(params.get(START_ADDRESS))
                .withDestinationAddress(params.get(DESTINATION_ADDRESS))
                .withStatus(params.get(STATUS))
                .withPaymentMethod(params.get(PAYMENT_METHOD))
                .withPassengerId(getNullOrUUID(passengerId))
                .withDriverId(getNullOrUUID(driverId))
                .withBookRide(getNullOrDateRange(bookRide))
                .withCancelRide(getNullOrDateRange(cancelRide))
                .withAcceptRide(getNullOrDateRange(acceptRide))
                .withBeginRide(getNullOrDateRange(beginRide))
                .withEndRide(getNullOrDateRange(endRide))
                .withFinishRide(getNullOrDateRange(finishRide))
                .build();
    }

    private UUID getNullOrUUID(String str) {
        return str != null
                ? UUID.fromString(str)
                : null;
    }

    private DateRange getNullOrDateRange(String str) {
        return str != null
                ? DateRange.fromSingleValue(str)
                : null;
    }
}
