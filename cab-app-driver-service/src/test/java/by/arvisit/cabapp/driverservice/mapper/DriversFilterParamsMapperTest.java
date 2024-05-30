package by.arvisit.cabapp.driverservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;

import by.arvisit.cabapp.driverservice.dto.DriversFilterParams;
import by.arvisit.cabapp.driverservice.util.DriverTestData;

class DriversFilterParamsMapperTest {

    private DriversFilterParamsMapper filterParamsMapper = new DriversFilterParamsMapper();

    @Test
    void shouldMapFromEmptyMapToFilterParamsDto() {
        Map<String, String> mapParams = Collections.emptyMap();

        DriversFilterParams result = filterParamsMapper.fromMapParams(mapParams);

        DriversFilterParams expected = DriverTestData.getEmptyDriversFilterParams().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldMapFromFilledMapToFilterParamsDto() {
        Map<String, String> mapParams = DriverTestData.getRequestParams();

        DriversFilterParams result = filterParamsMapper.fromMapParams(mapParams);

        DriversFilterParams expected = DriverTestData.getFilledDriversFilterParams().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
