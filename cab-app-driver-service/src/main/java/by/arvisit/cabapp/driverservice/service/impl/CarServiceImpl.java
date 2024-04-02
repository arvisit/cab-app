package by.arvisit.cabapp.driverservice.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.mapper.CarMapper;
import by.arvisit.cabapp.driverservice.persistence.model.Car;
import by.arvisit.cabapp.driverservice.persistence.repository.CarRepository;
import by.arvisit.cabapp.driverservice.service.CarService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {

    private static final String FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.driverservice.persistence.model.Car.id.EntityNotFoundException.template";

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final MessageSource messageSource;

    @Override
    public CarResponseDto getCarById(String id) {
        log.debug("Call for CarService.getCarById() with id {}", id);

        String errorMessage = messageSource.getMessage(
                FOUND_NO_ENTITY_BY_ID_MESSAGE_TEMPLATE_KEY,
                new Object[] { id }, null);
        Car car = carRepository.findById(UUID.fromString(id))
                .orElseThrow(
                        () -> new EntityNotFoundException(errorMessage));
        return carMapper.fromEntityToResponseDto(car);
    }

    @Transactional(readOnly = true)
    @Override
    public ListContainerResponseDto<CarResponseDto> getCars(Pageable pageable) {
        log.debug("Call for CarService.getCars() with pageable settings: {}", pageable);

        List<CarResponseDto> cars = carRepository.findAll(pageable).stream()
                .map(carMapper::fromEntityToResponseDto)
                .toList();

        return ListContainerResponseDto.<CarResponseDto>builder()
                .withValues(cars)
                .build();
    }

}
