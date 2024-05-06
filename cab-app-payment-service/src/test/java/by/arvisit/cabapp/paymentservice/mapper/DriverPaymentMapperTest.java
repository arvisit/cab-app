package by.arvisit.cabapp.paymentservice.mapper;

import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getDriverPayment;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getDriverPaymentRequestDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getDriverPaymentResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment;

class DriverPaymentMapperTest {

    private static final String[] FIELDS_TO_IGNORE = { "id", "timestamp", "status" };

    private DriverPaymentMapper driverPaymentMapper = Mappers.getMapper(DriverPaymentMapper.class);

    @Test
    void shouldMapFromEntityToResponseDto() {
        DriverPayment entity = getDriverPayment().build();

        DriverPaymentResponseDto actualResponseDto = driverPaymentMapper.fromEntityToResponseDto(entity);
        DriverPaymentResponseDto expectedResponseDto = getDriverPaymentResponseDto().build();

        assertThat(actualResponseDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseDto);
    }

    @Test
    void shouldMapFromRequestDtoToEntity() {
        DriverPaymentRequestDto requestDto = getDriverPaymentRequestDto().build();

        DriverPayment actualEntity = driverPaymentMapper.fromRequestDtoToEntity(requestDto);
        DriverPayment expectedEntity = getDriverPayment().build();

        assertThat(actualEntity)
                .usingRecursiveComparison()
                .ignoringFields(FIELDS_TO_IGNORE)
                .isEqualTo(expectedEntity);
    }
}
