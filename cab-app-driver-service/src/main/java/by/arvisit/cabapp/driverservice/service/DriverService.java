package by.arvisit.cabapp.driverservice.service;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;

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
