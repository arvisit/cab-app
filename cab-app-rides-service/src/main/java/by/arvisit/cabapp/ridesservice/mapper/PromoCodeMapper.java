package by.arvisit.cabapp.ridesservice.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;
import org.mapstruct.Mapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface PromoCodeMapper {

    PromoCodeResponseDto fromEntityToResponseDto(PromoCode entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    PromoCode fromRequestDtoToEntity(PromoCodeRequestDto dto);

    @Mapping(target = "isActive", ignore = true)
    void updateEntityWithRequestDto(PromoCodeRequestDto dto, @MappingTarget PromoCode entity);
}
