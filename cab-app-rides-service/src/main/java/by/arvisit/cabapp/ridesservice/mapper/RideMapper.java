package by.arvisit.cabapp.ridesservice.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import by.arvisit.cabapp.common.dto.payment.PassengerPaymentRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface RideMapper {

    @Mapping(target = "acceptRide", ignore = true)
    @Mapping(target = "beginRide", ignore = true)
    @Mapping(target = "bookRide", ignore = true)
    @Mapping(target = "cancelRide", ignore = true)
    @Mapping(target = "driverId", ignore = true)
    @Mapping(target = "driverScore", ignore = true)
    @Mapping(target = "endRide", ignore = true)
    @Mapping(target = "finalCost", ignore = true)
    @Mapping(target = "finishRide", ignore = true)
    @Mapping(target = "initialCost", ignore = true)
    @Mapping(target = "isPaid", ignore = true)
    @Mapping(target = "passengerScore", ignore = true)
    @Mapping(target = "promoCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    Ride fromRequestDtoToEntity(RideRequestDto dto);

    @Mapping(target = "promoCode",
            expression = "java(entity.getPromoCode() != null ? entity.getPromoCode().getKeyword() : null)")
    RideResponseDto fromEntityToResponseDto(Ride entity);

    @Mapping(target = "rideId", source = "id")
    @Mapping(target = "amount", source = "finalCost")
    @Mapping(target = "cardNumber", ignore = true)
    PassengerPaymentRequestDto fromRideToPassengerPaymentRequestDto(Ride entity);

    @Mapping(target = "rideId", source = "entity.id")
    @Mapping(target = "amount", source = "entity.finalCost")
    PassengerPaymentRequestDto fromRideToPassengerPaymentRequestDto(Ride entity, String cardNumber);
}
