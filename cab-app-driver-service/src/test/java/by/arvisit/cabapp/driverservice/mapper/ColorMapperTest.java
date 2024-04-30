package by.arvisit.cabapp.driverservice.mapper;

import static by.arvisit.cabapp.driverservice.util.DriverTestData.getColor;
import static by.arvisit.cabapp.driverservice.util.DriverTestData.getColorResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Color;

class ColorMapperTest {

    private ColorMapper colorMapper = Mappers.getMapper(ColorMapper.class);

    @Test
    void shouldMapFromEntityToResponseDto() {
        Color entity = getColor().build();
        ColorResponseDto expectedResponseDto = getColorResponseDto().build();

        ColorResponseDto actualResponseDto = colorMapper.fromEntityToResponseDto(entity);

        assertThat(actualResponseDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseDto);
    }
}
