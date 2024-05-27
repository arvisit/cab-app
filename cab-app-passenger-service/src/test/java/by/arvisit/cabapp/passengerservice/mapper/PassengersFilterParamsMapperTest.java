package by.arvisit.cabapp.passengerservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;

import by.arvisit.cabapp.passengerservice.dto.PassengersFilterParams;
import by.arvisit.cabapp.passengerservice.util.PassengerTestData;

class PassengersFilterParamsMapperTest {

    private PassengersFilterParamsMapper filterParamsMapper = new PassengersFilterParamsMapper();

    @Test
    void shouldMapFromEmptyMapToFilterParamsDto() {
        Map<String, String> mapParams = Collections.emptyMap();

        PassengersFilterParams result = filterParamsMapper.fromMapParams(mapParams);

        PassengersFilterParams expected = PassengerTestData.getEmptyPassengersFilterParams().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldMapFromFilledMapToFilterParamsDto() {
        Map<String, String> mapParams = PassengerTestData.getRequestParams();

        PassengersFilterParams result = filterParamsMapper.fromMapParams(mapParams);

        PassengersFilterParams expected = PassengerTestData.getFilledPassengersFilterParams().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
