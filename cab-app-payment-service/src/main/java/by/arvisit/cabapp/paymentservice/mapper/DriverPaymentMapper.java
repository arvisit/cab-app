package by.arvisit.cabapp.paymentservice.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface DriverPaymentMapper {

    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "status", ignore = true)
    DriverPayment fromRequestDtoToEntity(DriverPaymentRequestDto dto);

    DriverPaymentResponseDto fromEntityToResponseDto(DriverPayment entity);
}
