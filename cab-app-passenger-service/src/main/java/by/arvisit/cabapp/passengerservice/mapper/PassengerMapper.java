package by.arvisit.cabapp.passengerservice.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface PassengerMapper {

    PassengerResponseDto fromEntityToResponseDto(Passenger entity);

    @Mapping(target = "id", ignore = true)
    Passenger fromRequestDtoToEntity(PassengerRequestDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityWithRequestDto(PassengerRequestDto dto, @MappingTarget Passenger entity);
}
