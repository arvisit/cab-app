package by.arvisit.cabapp.driverservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.service.CarManufacturerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/car-manufacturers")
@RequiredArgsConstructor
@Slf4j
public class CarManufacturerController {

    private final CarManufacturerService carManufacturerService;

    @GetMapping
    public ListContainerResponseDto<CarManufacturerResponseDto> getCarManufacturers() {
        ListContainerResponseDto<CarManufacturerResponseDto> response = carManufacturerService.getCarManufacturers();

        log.debug("Got all car manufacturers. Total count: {}", response.values().size());
        return response;
    }
}
