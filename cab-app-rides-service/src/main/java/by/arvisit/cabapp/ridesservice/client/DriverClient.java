package by.arvisit.cabapp.ridesservice.client;

import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;

public interface DriverClient {

    DriverResponseDto getDriverById(String id);
}
