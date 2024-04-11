package by.arvisit.cabapp.paymentservice.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface PassengerPaymentMapper {

    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "status", ignore = true)
    PassengerPayment fromRequestDtoToEntity(PassengerPaymentRequestDto dto);

    PassengerPaymentResponseDto fromEntityToResponseDto(PassengerPayment entity);
}
