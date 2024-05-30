package by.arvisit.cabapp.driverservice.service.impl;

import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_CAR_MANUFACTURER_ID;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_CAR_MANUFACTURER_NAME;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarManufacturer;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarManufacturerResponseDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarManufacturerResponseDtoInListContainer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.mapper.CarManufacturerMapper;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;
import by.arvisit.cabapp.driverservice.persistence.repository.CarManufacturerRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CarManufacturerServiceImplTest {

    @InjectMocks
    private CarManufacturerServiceImpl carManufacturerService;
    @Mock
    private CarManufacturerRepository carManufacturerRepository;
    @Mock
    private CarManufacturerMapper carManufacturerMapper;
    @Mock
    private MessageSource messageSource;

    @Test
    void shouldReturnEntity_whenGetCarManufacturerEntityByExistingId() {
        CarManufacturer carManufacturer = getCarManufacturer().build();

        when(carManufacturerRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(carManufacturer));

        CarManufacturer actualCarManufacturer = carManufacturerService
                .getCarManufacturerEntityById(DEFAULT_CAR_MANUFACTURER_ID);

        assertThat(actualCarManufacturer.getId())
                .isEqualTo(DEFAULT_CAR_MANUFACTURER_ID);
        assertThat(actualCarManufacturer.getName())
                .isEqualTo(DEFAULT_CAR_MANUFACTURER_NAME);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetCarManufacturerEntityByNonExistingId() {
        when(carManufacturerRepository.findById(any(Integer.class)))
                .thenThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> carManufacturerService.getCarManufacturerEntityById(DEFAULT_CAR_MANUFACTURER_ID))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnContainerWithCarManufacturerResponseDto_whenGetCarManufacturers() {
        List<CarManufacturer> carManufacturers = List.of(getCarManufacturer().build());
        Page<CarManufacturer> carManufacturersPage = new PageImpl<>(carManufacturers);
        CarManufacturerResponseDto carManufacturerResponseDto = getCarManufacturerResponseDto().build();

        when(carManufacturerRepository.findAll(any(Pageable.class)))
                .thenReturn(carManufacturersPage);
        when(carManufacturerMapper.fromEntityToResponseDto(any(CarManufacturer.class)))
                .thenReturn(carManufacturerResponseDto);

        Pageable pageable = Pageable.ofSize(DEFAULT_PAGEABLE_SIZE);
        assertThat(carManufacturerService.getCarManufacturers(pageable))
                .isEqualTo(getCarManufacturerResponseDtoInListContainer().build());
    }

    @Test
    void shouldReturnTrue_whenCarManufacturerExists() {
        when(carManufacturerRepository.existsById(any(Integer.class)))
                .thenReturn(true);

        assertThat(carManufacturerService.existsById(DEFAULT_CAR_MANUFACTURER_ID))
                .isTrue();
    }

    @Test
    void shouldReturnFalse_whenCarManufacturerDoesNotExist() {
        when(carManufacturerRepository.existsById(any(Integer.class)))
                .thenReturn(false);

        assertThat(
                carManufacturerService.existsById(DEFAULT_CAR_MANUFACTURER_ID))
                .isFalse();
    }

}
