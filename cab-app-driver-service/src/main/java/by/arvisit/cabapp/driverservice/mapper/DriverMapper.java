package by.arvisit.cabapp.driverservice.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Driver;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true),
        uses = { CarMapper.class })
public abstract class DriverMapper {

    public abstract DriverResponseDto fromEntityToResponseDto(Driver entity);

    public abstract Driver fromRequestDtoToEntity(DriverRequestDto dto);

    public abstract void updateEntityWithRequestDto(DriverRequestDto dto, @MappingTarget Driver entity);
}
