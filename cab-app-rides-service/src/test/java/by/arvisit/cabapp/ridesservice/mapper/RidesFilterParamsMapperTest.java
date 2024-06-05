package by.arvisit.cabapp.ridesservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;

import by.arvisit.cabapp.ridesservice.dto.RidesFilterParams;
import by.arvisit.cabapp.ridesservice.util.RideTestData;

class RidesFilterParamsMapperTest {

    private RidesFilterParamsMapper filterParamsMapper = new RidesFilterParamsMapper();

    @Test
    void shouldMapFromEmptyMapToFilterParamsDto() {
        Map<String, String> mapParams = Collections.emptyMap();

        RidesFilterParams result = filterParamsMapper.fromMapParams(mapParams);

        RidesFilterParams expected = RideTestData.getEmptyRidesFilterParams().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldMapFromFilledMapToFilterParamsDto() {
        Map<String, String> mapParams = RideTestData.getRequestParams();

        RidesFilterParams result = filterParamsMapper.fromMapParams(mapParams);

        RidesFilterParams expected = RideTestData.getFilledRidesFilterParams().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
