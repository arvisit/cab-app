package by.arvisit.cabapp.driverservice.service;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;

public interface CarManufacturerService {

    CarManufacturer getCarManufacturerEntityById(Integer id);

    ListContainerResponseDto<CarManufacturerResponseDto> getCarManufacturers(Pageable pageable);

    boolean existsById(Integer id);
}
