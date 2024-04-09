package by.arvisit.cabapp.driverservice.service;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.dto.ListContainerResponseDto;

public interface DriverService {

    ListContainerResponseDto<DriverResponseDto> getDrivers(Pageable pageable);

    ListContainerResponseDto<DriverResponseDto> getAvailableDrivers(Pageable pageable);

    DriverResponseDto getDriverById(String id);

    DriverResponseDto getDriverByEmail(String email);

    DriverResponseDto save(DriverRequestDto dto);

    DriverResponseDto update(String id, DriverRequestDto dto);

    DriverResponseDto updateAvailability(String id, Boolean value);

    void delete(String id);
}
