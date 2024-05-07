package by.arvisit.cabapp.driverservice.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.service.CarManufacturerService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/car-manufacturers")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CarManufacturerController {

    private final CarManufacturerService carManufacturerService;

    @GetMapping
    public ListContainerResponseDto<CarManufacturerResponseDto> getCarManufacturers(
            @PageableDefault @Nullable @Valid Pageable pageable) {
        ListContainerResponseDto<CarManufacturerResponseDto> response = carManufacturerService
                .getCarManufacturers(pageable);

        log.debug("Got all car manufacturers. Total count: {}. Pageable settings: {}", response.values().size(),
                pageable);
        return response;
    }
}
