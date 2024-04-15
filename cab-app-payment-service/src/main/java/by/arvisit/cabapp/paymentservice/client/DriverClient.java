package by.arvisit.cabapp.paymentservice.client;

import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;

public interface DriverClient {

    DriverResponseDto getDriverById(String id);
}
