package by.arvisit.cabapp.passengerservice.mapper;

import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.NEW_CARD_NUMBER;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.NEW_EMAIL;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.NEW_NAME;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassenger;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassengerRequestDto;
import static by.arvisit.cabapp.passengerservice.util.PassengerTestData.getPassengerResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;

@ExtendWith(MockitoExtension.class)
class PassengerMapperTest {

    private PassengerMapper passengerMapper = Mappers.getMapper(PassengerMapper.class);

    @Test
    void shouldMapFromEntityToResponseDto() {
        Passenger entity = getPassenger().build();

        PassengerResponseDto result = passengerMapper.fromEntityToResponseDto(entity);

        PassengerResponseDto expected = getPassengerResponseDto().build();

        assertThat(result)
                .isEqualTo(expected);
    }

    @Test
    void shouldMapFromRequestDtoToEntity() {
        PassengerRequestDto requestDto = getPassengerRequestDto().build();

        Passenger result = passengerMapper.fromRequestDtoToEntity(requestDto);

        Passenger expected = getPassenger()
                .withId(null)
                .build();

        assertThat(result)
                .isEqualTo(expected);
    }

    @Test
    void shouldUpdateEntityWithRequestDto() {
        PassengerRequestDto requestDto = getPassengerRequestDto()
                .withName(NEW_NAME)
                .withEmail(NEW_EMAIL)
                .withCardNumber(NEW_CARD_NUMBER)
                .build();

        Passenger entity = getPassenger().build();

        passengerMapper.updateEntityWithRequestDto(requestDto, entity);

        Passenger expected = getPassenger()
                .withName(NEW_NAME)
                .withEmail(NEW_EMAIL)
                .withCardNumber(NEW_CARD_NUMBER)
                .build();

        assertThat(entity)
                .isEqualTo(expected);
    }
}
