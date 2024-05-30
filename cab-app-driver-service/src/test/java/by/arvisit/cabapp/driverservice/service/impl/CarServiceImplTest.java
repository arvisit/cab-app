package by.arvisit.cabapp.driverservice.service.impl;

import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_CAR_ID_STRING;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCar;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarResponseDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.mapper.CarMapper;
import by.arvisit.cabapp.driverservice.persistence.model.Car;
import by.arvisit.cabapp.driverservice.persistence.repository.CarRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl carService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarMapper carMapper;
    @Mock
    private MessageSource messageSource;

    @Test
    void shouldReturnEntity_whenGetCarEntityByExistingId() {
        Car car = getCar().build();
        CarResponseDto carResponseDto = getCarResponseDto().build();

        when(carRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(car));
        when(carMapper.fromEntityToResponseDto(any(Car.class)))
                .thenReturn(carResponseDto);

        assertThat(carService.getCarById(DEFAULT_CAR_ID_STRING))
                .usingRecursiveComparison()
                .isEqualTo(carResponseDto);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetCarEntityByNonExistingId() {
        when(carRepository.findById(any(UUID.class)))
                .thenThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> carService.getCarById(DEFAULT_CAR_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnContainerWithCarResponseDto_whenGetCars() {
        List<Car> cars = List.of(getCar().build());
        Page<Car> carsPage = new PageImpl<>(cars);
        CarResponseDto carResponseDto = getCarResponseDto().build();

        when(carRepository.findAll(any(Pageable.class)))
                .thenReturn(carsPage);
        when(carMapper.fromEntityToResponseDto(any(Car.class)))
                .thenReturn(carResponseDto);

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        assertThat(carService.getCars(pageable))
                .isEqualTo(getCarResponseDtoInListContainer().build());
    }

}
