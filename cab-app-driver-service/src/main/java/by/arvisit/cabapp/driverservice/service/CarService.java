package by.arvisit.cabapp.driverservice.service;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.dto.ListContainerResponseDto;

public interface CarService {

    CarResponseDto getCarById(String id);

    ListContainerResponseDto<CarResponseDto> getCars(Pageable pageable);
}
