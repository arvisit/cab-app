package by.arvisit.cabapp.paymentservice.client;

import by.arvisit.cabapp.common.dto.rides.RideResponseDto;

public interface RideClient {

    RideResponseDto getRideById(String id);

}
