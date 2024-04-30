package by.arvisit.cabapp.driverservice.mapper;

import static by.arvisit.cabapp.driverservice.util.DriverTestData.COLOR_ID_BLACK;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.COLOR_NAME_BLACK;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCar;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarManufacturer;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarRequestDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarResponseDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getColor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import by.arvisit.cabapp.driverservice.dto.CarRequestDto;
import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Car;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;
import by.arvisit.cabapp.driverservice.persistence.model.Color;
import by.arvisit.cabapp.driverservice.service.CarManufacturerService;
import by.arvisit.cabapp.driverservice.service.ColorService;

@ExtendWith(MockitoExtension.class)
class CarMapperTest {

    private static final String ID_FIELD = "id";

    @InjectMocks
    private CarMapper carMapper = Mappers.getMapper(CarMapper.class);
    @Mock
    private ColorService colorService;
    @Mock
    private CarManufacturerService carManufacturerService;

    @Test
    void shouldMapFromEntityToResponseDto() {
        Car entity = getCar().build();

        CarResponseDto actualResponseDto = carMapper.fromEntityToResponseDto(entity);
        CarResponseDto expectedResponseDto = getCarResponseDto().build();

        assertThat(actualResponseDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseDto);
    }

    @Test
    void shouldMapFromRequestDtoToEntity() {
        Color color = getColor().build();
        CarManufacturer carManufacturer = getCarManufacturer().build();

        when(colorService.getColorEntityById(any(Integer.class)))
                .thenReturn(color);
        when(carManufacturerService.getCarManufacturerEntityById(any(Integer.class)))
                .thenReturn(carManufacturer);

        CarRequestDto requestDto = getCarRequestDto().build();
        Car actualEntity = carMapper.fromRequestDtoToEntity(requestDto);
        Car expectedEntity = getCar().build();

        assertThat(actualEntity)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD)
                .isEqualTo(expectedEntity);

    }

    @Test
    void shouldUpdateEntityWithRequestDto() {
        Color color = getColor()
                .withId(COLOR_ID_BLACK)
                .withName(COLOR_NAME_BLACK)
                .build();
        CarManufacturer carManufacturer = getCarManufacturer().build();

        when(colorService.getColorEntityById(any(Integer.class)))
                .thenReturn(color);
        when(carManufacturerService.getCarManufacturerEntityById(any(Integer.class)))
                .thenReturn(carManufacturer);

        CarRequestDto requestDto = getCarRequestDto()
                .withColorId(COLOR_ID_BLACK)
                .build();
        Car actualEntity = carMapper.fromRequestDtoToEntity(requestDto);
        Car expectedEntity = getCar()
                .withColor(color)
                .build();

        assertThat(actualEntity)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD)
                .isEqualTo(expectedEntity);
    }
}
