package by.arvisit.cabapp.ridesservice.mapper;

import static by.arvisit.cabapp.ridesservice.util.RideTestData.DEFAULT_PASSENGER_CARD_NUMBER;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getBookedRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getEndedRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getFinishedWithScoresAndPromoCodeRide;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getFinishedWithScoresAndPromoCodeRideResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPassengerPaymentRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getRideRequestDto;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import by.arvisit.cabapp.common.dto.payment.PassengerPaymentRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;

class RideMapperTest {

    private static final String CARD_NUMBER_FIELD = "cardNumber";
    private static final String ID_FIELD = "id";
    private static final String[] FROM_REQUEST_TO_ENTITY_FIELDS_TO_IGNORE = {
            "id",
            "acceptRide",
            "beginRide",
            "bookRide",
            "cancelRide",
            "driverId",
            "driverScore",
            "endRide",
            "finalCost",
            "finishRide",
            "initialCost",
            "isPaid",
            "passengerScore",
            "promoCode",
            "status"
    };

    private RideMapper promoCodeMapper = Mappers.getMapper(RideMapper.class);

    @Test
    void shouldMapFromEntityToResponseDto() {
        Ride entity = getFinishedWithScoresAndPromoCodeRide().build();

        RideResponseDto actualResponseDto = promoCodeMapper.fromEntityToResponseDto(entity);
        RideResponseDto expectedResponseDto = getFinishedWithScoresAndPromoCodeRideResponseDto().build();

        assertThat(actualResponseDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseDto);
    }

    @Test
    void shouldMapFromRequestDtoToEntity() {
        RideRequestDto requestDto = getRideRequestDto().build();

        Ride actualEntity = promoCodeMapper.fromRequestDtoToEntity(requestDto);
        Ride expectedEntity = getBookedRide().build();

        assertThat(actualEntity)
                .usingRecursiveComparison()
                .ignoringFields(FROM_REQUEST_TO_ENTITY_FIELDS_TO_IGNORE)
                .isEqualTo(expectedEntity);
    }

    @Test
    void shouldMapFromRideToPassengerPaymentRequestDtoWithoutCardNumber() {
        Ride ride = getEndedRide().build();

        PassengerPaymentRequestDto actualPaymentRequestDto = promoCodeMapper.fromRideToPassengerPaymentRequestDto(ride);
        PassengerPaymentRequestDto expectedPaymentRequestDto = getPassengerPaymentRequestDto().build();

        assertThat(actualPaymentRequestDto)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, CARD_NUMBER_FIELD)
                .isEqualTo(expectedPaymentRequestDto);
    }

    @Test
    void shouldMapFromRideToPassengerPaymentRequestDtoWithCardNumber() {
        Ride ride = getEndedRide().build();

        PassengerPaymentRequestDto actualPaymentRequestDto = promoCodeMapper.fromRideToPassengerPaymentRequestDto(ride,
                DEFAULT_PASSENGER_CARD_NUMBER);
        PassengerPaymentRequestDto expectedPaymentRequestDto = getPassengerPaymentRequestDto().build();

        assertThat(actualPaymentRequestDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedPaymentRequestDto);
    }
}
