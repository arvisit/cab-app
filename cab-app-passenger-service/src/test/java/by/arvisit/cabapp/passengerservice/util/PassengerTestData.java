package by.arvisit.cabapp.passengerservice.util;

import java.util.List;
import java.util.UUID;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;

public final class PassengerTestData {

    public static final int DEFAULT_PAGEABLE_SIZE = 10;
    public static final String UNSORTED = "UNSORTED";
    public static final String NOT_ALLOWED_REQUEST_PARAM = "passengerId";
    public static final String NAME_REQUEST_PARAM = "name";
    public static final String DEFAULT_CARD_NUMBER = "7853471929691513";
    public static final String NEW_CARD_NUMBER = "1111222233334444";
    public static final String DEFAULT_EMAIL = "vivienne.gutierrez@yahoo.com.ar";
    public static final String NEW_EMAIL = "jane.doe@mail.com";
    public static final String DEFAULT_NAME = "Vivienne Gutierrez";
    public static final String NEW_NAME = "Jane Doe";
    public static final String DEFAULT_ID_STRING = "3abcc6a1-94da-4185-aaa1-8a11c1b8efd2";
    public static final String NEW_ID_STRING = "1abcc8a1-54da-4185-a3a1-8a11c1a8efa2";
    public static final UUID DEFAULT_ID_UUID = UUID.fromString(DEFAULT_ID_STRING);
    public static final UUID NEW_ID_UUID = UUID.fromString(NEW_ID_STRING);
    public static final String URL_PASSENGERS = "/api/v1/passengers";
    public static final String URL_PASSENGERS_ID_TEMPLATE = "/api/v1/passengers/{id}";
    public static final String URL_PASSENGERS_EMAIL_TEMPLATE = "/api/v1/passengers/by-email/{email}";
    public static final String URL_PASSENGERS_PARAM_VALUE_TEMPLATE = "/api/v1/passengers?{param}={value}";
    public static final String ENCODING_UTF_8 = "UTF-8";

    private PassengerTestData() {
    }

    public static Passenger.PassengerBuilder getPassenger() {
        return Passenger.builder()
                .withId(DEFAULT_ID_UUID)
                .withName(DEFAULT_NAME)
                .withEmail(DEFAULT_EMAIL)
                .withCardNumber(DEFAULT_CARD_NUMBER);
    }

    public static PassengerRequestDto.PassengerRequestDtoBuilder getPassengerRequestDto() {
        return PassengerRequestDto.builder()
                .withName(DEFAULT_NAME)
                .withEmail(DEFAULT_EMAIL)
                .withCardNumber(DEFAULT_CARD_NUMBER);
    }

    public static PassengerResponseDto.PassengerResponseDtoBuilder getPassengerResponseDto() {
        return PassengerResponseDto.builder()
                .withId(DEFAULT_ID_STRING)
                .withName(DEFAULT_NAME)
                .withEmail(DEFAULT_EMAIL)
                .withCardNumber(DEFAULT_CARD_NUMBER);
    }

    public static ListContainerResponseDto.ListContainerResponseDtoBuilder<PassengerResponseDto> getPassengerResponseDtoInListContainer() {
        return ListContainerResponseDto.<PassengerResponseDto>builder()
                .withValues(List.of(getPassengerResponseDto().build()))
                .withCurrentPage(0)
                .withSize(DEFAULT_PAGEABLE_SIZE)
                .withLastPage(0)
                .withSort(UNSORTED);
    }
}
