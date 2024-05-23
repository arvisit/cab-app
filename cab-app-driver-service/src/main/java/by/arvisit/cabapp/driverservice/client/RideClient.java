package by.arvisit.cabapp.driverservice.client;

import java.util.Map;

import by.arvisit.cabapp.common.dto.rides.RideResponseDto;

public interface RideClient {

    RideResponseDto getRideById(String id);

    RideResponseDto acceptRide(String id, Map<String, String> patch);

}
