package by.arvisit.cabapp.driverservice.controller;

import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.service.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CarController {

    private final CarService carService;

    @GetMapping
    public ListContainerResponseDto<CarResponseDto> getCars() {
        ListContainerResponseDto<CarResponseDto> response = carService.getCars();

        log.debug("Got all cars. Total count: {}", response.values().size());
        return response;
    }

    @GetMapping("/{id}")
    public CarResponseDto getCarById(@PathVariable @UUID String id) {
        CarResponseDto response = carService.getCarById(id);

        log.debug("Got car by id {}: {}", id, response);
        return response;
    }
}
