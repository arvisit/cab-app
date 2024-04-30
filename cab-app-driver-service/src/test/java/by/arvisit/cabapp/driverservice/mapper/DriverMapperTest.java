package by.arvisit.cabapp.driverservice.mapper;

import static by.arvisit.cabapp.driverservice.util.DriverTestData.NEW_DRIVER_NAME;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCar;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getCarResponseDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getDriver;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getDriverRequestDto;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getDriverResponseDto;
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
import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Car;
import by.arvisit.cabapp.driverservice.persistence.model.Driver;

@ExtendWith(MockitoExtension.class)
class DriverMapperTest {

    private static final String IS_AVAILABLE_FIELD = "isAvailable";
    private static final String ID_FIELD = "id";

    @InjectMocks
    private DriverMapper driverMapper = Mappers.getMapper(DriverMapper.class);
    @Mock
    private CarMapper carMapper;

    @Test
    void shouldMapFromEntityToResponseDto() {
        Driver entity = getDriver().build();

        when(carMapper.fromEntityToResponseDto(any(Car.class)))
                .thenReturn(getCarResponseDto().build());

        DriverResponseDto actualResponseDto = driverMapper.fromEntityToResponseDto(entity);
        DriverResponseDto expectedResponseDto = getDriverResponseDto().build();

        assertThat(actualResponseDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseDto);
    }

    @Test
    void shouldMapFromRequestDtoToEntity() {
        DriverRequestDto requestDto = getDriverRequestDto().build();

        when(carMapper.fromRequestDtoToEntity(any(CarRequestDto.class)))
                .thenReturn(getCar().build());

        Driver actualEntity = driverMapper.fromRequestDtoToEntity(requestDto);
        Driver expectedEntity = getDriver().build();

        assertThat(actualEntity)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, IS_AVAILABLE_FIELD)
                .isEqualTo(expectedEntity);
    }

    @Test
    void shouldUpdateEntityWithRequestDto() {
        DriverRequestDto requestDto = getDriverRequestDto()
                .withName(NEW_DRIVER_NAME)
                .build();
        Driver entityToUpdate = getDriver().build();

        driverMapper.updateEntityWithRequestDto(requestDto, entityToUpdate);

        assertThat(entityToUpdate.getName())
                .isEqualTo(requestDto.name());
    }
}
