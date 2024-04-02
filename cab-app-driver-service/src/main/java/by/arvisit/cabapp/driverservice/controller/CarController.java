package by.arvisit.cabapp.driverservice.controller;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.service.CarService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
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
    public ListContainerResponseDto<CarResponseDto> getCars(@PageableDefault @Nullable @Valid Pageable pageable) {
        ListContainerResponseDto<CarResponseDto> response = carService.getCars(pageable);

        log.debug("Got all cars. Total count: {}. Pageable settings: {}", response.values().size(), pageable);
        return response;
    }

    @GetMapping("/{id}")
    public CarResponseDto getCarById(@PathVariable @UUID String id) {
        CarResponseDto response = carService.getCarById(id);

        log.debug("Got car by id {}: {}", id, response);
        return response;
    }
}
