package by.arvisit.cabapp.paymentservice.mapper;

import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getPassengerPayment;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getPassengerPaymentRequestDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentTestData.getPassengerPaymentResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment;

class PassengerPaymentMapperTest {

    private static final String[] FIELDS_TO_IGNORE = { "id", "feeAmount", "timestamp", "status" };

    private PassengerPaymentMapper passengerPaymentMapper = Mappers.getMapper(PassengerPaymentMapper.class);

    @Test
    void shouldMapFromEntityToResponseDto() {
        PassengerPayment entity = getPassengerPayment().build();

        PassengerPaymentResponseDto actualResponseDto = passengerPaymentMapper.fromEntityToResponseDto(entity);
        PassengerPaymentResponseDto expectedResponseDto = getPassengerPaymentResponseDto().build();

        assertThat(actualResponseDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseDto);
    }

    @Test
    void shouldMapFromRequestDtoToEntity() {
        PassengerPaymentRequestDto requestDto = getPassengerPaymentRequestDto().build();

        PassengerPayment actualEntity = passengerPaymentMapper.fromRequestDtoToEntity(requestDto);
        PassengerPayment expectedEntity = getPassengerPayment().build();

        assertThat(actualEntity)
                .usingRecursiveComparison()
                .ignoringFields(FIELDS_TO_IGNORE)
                .isEqualTo(expectedEntity);
    }
}
