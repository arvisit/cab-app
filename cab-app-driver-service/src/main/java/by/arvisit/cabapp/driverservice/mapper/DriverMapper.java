package by.arvisit.cabapp.driverservice.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Driver;
import org.mapstruct.Mapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true),
        uses = { CarMapper.class })
public interface DriverMapper {

    DriverResponseDto fromEntityToResponseDto(Driver entity);

    @Mapping(target = "isAvailable", ignore = true)
    Driver fromRequestDtoToEntity(DriverRequestDto dto);

    @Mapping(target = "isAvailable", ignore = true)
    void updateEntityWithRequestDto(DriverRequestDto dto, @MappingTarget Driver entity);
}
