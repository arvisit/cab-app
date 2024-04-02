package by.arvisit.cabapp.driverservice.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface CarManufacturerMapper {

    CarManufacturerResponseDto fromEntityToResponseDto(CarManufacturer entity);
}
