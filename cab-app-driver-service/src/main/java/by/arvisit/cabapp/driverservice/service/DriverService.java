package by.arvisit.cabapp.driverservice.service;

import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.dto.ListContainerResponseDto;

public interface DriverService {

    ListContainerResponseDto<DriverResponseDto> getDrivers();

    DriverResponseDto getDriverById(String id);

    DriverResponseDto getDriverByEmail(String email);

    DriverResponseDto save(DriverRequestDto dto);

    DriverResponseDto update(String id, DriverRequestDto dto);

    void delete(String id);
}
