package by.arvisit.cabapp.driverservice.service;

import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;

public interface CarManufacturerService {

    CarManufacturer getCarManufacturerEntityById(Integer id);

    ListContainerResponseDto<CarManufacturerResponseDto> getCarManufacturers();

    boolean existsById(Integer id);
}
