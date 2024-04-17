package by.arvisit.cabapp.ridesservice.client;

import java.util.Map;

import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;

public interface DriverClient {

    DriverResponseDto getDriverById(String id);

    DriverResponseDto updateAvailability(String id, Map<String, Boolean> patch);

}
