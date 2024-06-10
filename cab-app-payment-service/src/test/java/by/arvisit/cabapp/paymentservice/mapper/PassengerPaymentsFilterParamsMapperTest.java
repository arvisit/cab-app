package by.arvisit.cabapp.paymentservice.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;

import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentsFilterParams;
import by.arvisit.cabapp.paymentservice.util.PaymentTestData;

class PassengerPaymentsFilterParamsMapperTest {

    private PassengerPaymentsFilterParamsMapper filterParamsMapper = new PassengerPaymentsFilterParamsMapper();

    @Test
    void shouldMapFromEmptyMapToFilterParamsDto() {
        Map<String, String> mapParams = Collections.emptyMap();

        PassengerPaymentsFilterParams result = filterParamsMapper.fromMapParams(mapParams);

        PassengerPaymentsFilterParams expected = PaymentTestData.getEmptyPassengerPaymentsFilterParams().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldMapFromFilledMapToFilterParamsDto() {
        Map<String, String> mapParams = PaymentTestData.getPassengerPaymentsRequestParams();

        PassengerPaymentsFilterParams result = filterParamsMapper.fromMapParams(mapParams);

        PassengerPaymentsFilterParams expected = PaymentTestData.getFilledPassengerPaymentsFilterParams().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
