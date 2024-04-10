package by.arvisit.cabapp.driverservice.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import by.arvisit.cabapp.driverservice.dto.CarRequestDto;
import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Car;
import by.arvisit.cabapp.driverservice.service.CarManufacturerService;
import by.arvisit.cabapp.driverservice.service.ColorService;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public abstract class CarMapper {
    @Autowired
    protected ColorService colorService;
    @Autowired
    protected CarManufacturerService carManufacturerService;

    public abstract CarResponseDto fromEntityToResponseDto(Car entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manufacturer", expression = "java(carManufacturerService.getCarManufacturerEntityById(dto.manufacturerId()))")
    @Mapping(target = "color", expression = "java(colorService.getColorEntityById(dto.colorId()))")
    public abstract Car fromRequestDtoToEntity(CarRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manufacturer", expression = "java(carManufacturerService.getCarManufacturerEntityById(dto.manufacturerId()))")
    @Mapping(target = "color", expression = "java(colorService.getColorEntityById(dto.colorId()))")
    public abstract void updateEntityWithRequestDto(CarRequestDto dto, @MappingTarget Car entity);
}
