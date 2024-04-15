package by.arvisit.cabapp.ridesservice.client;

import by.arvisit.cabapp.ridesservice.dto.DriverResponseDto;

public interface DriverClient {

    DriverResponseDto getDriverById(String id);
}
