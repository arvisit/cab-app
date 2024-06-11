package by.arvisit.cabapp.paymentservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;

import by.arvisit.cabapp.paymentservice.dto.DriverPaymentsFilterParams;
import by.arvisit.cabapp.paymentservice.util.PaymentTestData;

class DriverPaymentsFilterParamsMapperTest {

    private DriverPaymentsFilterParamsMapper filterParamsMapper = new DriverPaymentsFilterParamsMapper();

    @Test
    void shouldMapFromEmptyMapToFilterParamsDto() {
        Map<String, String> mapParams = Collections.emptyMap();

        DriverPaymentsFilterParams result = filterParamsMapper.fromMapParams(mapParams);

        DriverPaymentsFilterParams expected = PaymentTestData.getEmptyDriverPaymentsFilterParams().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldMapFromFilledMapToFilterParamsDto() {
        Map<String, String> mapParams = PaymentTestData.getDriverPaymentsRequestParams();

        DriverPaymentsFilterParams result = filterParamsMapper.fromMapParams(mapParams);

        DriverPaymentsFilterParams expected = PaymentTestData.getFilledDriverPaymentsFilterParams().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
