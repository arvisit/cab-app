package by.arvisit.cabapp.driverservice.mapper;

import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarManufacturer;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarManufacturerResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;

class CarManufacturerMapperTest {

    private CarManufacturerMapper carManufacturerMapper = Mappers.getMapper(CarManufacturerMapper.class);

    @Test
    void shouldMapFromEntityToResponseDto() {
        CarManufacturer entity = getCarManufacturer().build();
        CarManufacturerResponseDto expectedResponseDto = getCarManufacturerResponseDto().build();

        CarManufacturerResponseDto actualResponseDto = carManufacturerMapper.fromEntityToResponseDto(entity);

        assertThat(actualResponseDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseDto);
    }
}
