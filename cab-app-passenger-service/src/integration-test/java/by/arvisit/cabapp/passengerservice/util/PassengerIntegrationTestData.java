package by.arvisit.cabapp.passengerservice.util;

import java.util.Collections;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;

public final class PassengerIntegrationTestData {

    private PassengerIntegrationTestData() {
    }

    public static final String URL_PASSENGERS = "/api/v1/passengers";
    public static final String URL_PASSENGERS_ID_TEMPLATE = "/api/v1/passengers/{id}";
    public static final String URL_PASSENGERS_EMAIL_TEMPLATE = "/api/v1/passengers/by-email/{email}";

    public static final String JOHNNY_DOE_ID_STRING = "44012418-4211-4e65-aac6-c1f19f25715c";
    public static final String JOHN_DOE_ID_STRING = "51334f37-482c-4498-ad0f-122929edc0ff";
    public static final String JANNY_DOE_ID_STRING = "edeb6315-5408-46e6-a710-2666725c4dc7";
    public static final String JANE_DOE_ID_STRING = "3abcc6a1-94da-4185-aaa1-8a11c1b8efd2";

    public static final String JOHN_DOE_EMAIL = "john.doe@mail.com";

    public static final String NEW_PASSENGER_NAME = "Jack Black";
    public static final String NEW_PASSENGER_EMAIL = "jack.black@mail.com";
    public static final String NEW_PASSENGER_CARD_NUMBER = "0000111122223333";
    
    public static final int DEFAULT_PAGEABLE_SIZE = 10;
    public static final String UNSORTED = "UNSORTED";

    public static PassengerRequestDto.PassengerRequestDtoBuilder getSavePassengerRequest() {
        return PassengerRequestDto.builder()
                .withName(NEW_PASSENGER_NAME)
                .withEmail(NEW_PASSENGER_EMAIL)
                .withCardNumber(NEW_PASSENGER_CARD_NUMBER);
    }

    public static PassengerResponseDto.PassengerResponseDtoBuilder getAddedPassengerResponse() {
        return PassengerResponseDto.builder()
                .withId(null)
                .withName(NEW_PASSENGER_NAME)
                .withEmail(NEW_PASSENGER_EMAIL)
                .withCardNumber(NEW_PASSENGER_CARD_NUMBER);
    }

    public static PassengerRequestDto.PassengerRequestDtoBuilder getUpdatePassengerRequest() {
        return PassengerRequestDto.builder()
                .withName(NEW_PASSENGER_NAME)
                .withEmail(NEW_PASSENGER_EMAIL)
                .withCardNumber(NEW_PASSENGER_CARD_NUMBER);
    }
    
    public static PassengerResponseDto.PassengerResponseDtoBuilder getUpdatedPassengerResponse() {
        return PassengerResponseDto.builder()
                .withId(JOHN_DOE_ID_STRING)
                .withName(NEW_PASSENGER_NAME)
                .withEmail(NEW_PASSENGER_EMAIL)
                .withCardNumber(NEW_PASSENGER_CARD_NUMBER);
    }

    public static PassengerResponseDto.PassengerResponseDtoBuilder getJohnnyDoe() {
        return PassengerResponseDto.builder()
                .withId(JOHNNY_DOE_ID_STRING)
                .withName("Johnny Doe")
                .withEmail("johnny.doe@yahoo.de")
                .withCardNumber(null);
    }

    public static PassengerResponseDto.PassengerResponseDtoBuilder getJohnDoe() {
        return PassengerResponseDto.builder()
                .withId(JOHN_DOE_ID_STRING)
                .withName("John Doe")
                .withEmail("john.doe@mail.com")
                .withCardNumber("8633928741544997");
    }

    public static PassengerResponseDto.PassengerResponseDtoBuilder getJannyDoe() {
        return PassengerResponseDto.builder()
                .withId(JANNY_DOE_ID_STRING)
                .withName("Janny Doe")
                .withEmail("janny.doe@yahoo.com.br")
                .withCardNumber(null);
    }

    public static PassengerResponseDto.PassengerResponseDtoBuilder getJaneDoe() {
        return PassengerResponseDto.builder()
                .withId(JANE_DOE_ID_STRING)
                .withName("Jane Doe")
                .withEmail("jane.doe@yahoo.com.ar")
                .withCardNumber("7853471929691513");
    }

    public static ListContainerResponseDto.ListContainerResponseDtoBuilder<PassengerResponseDto> getListContainerForPassengers() {
        return ListContainerResponseDto.<PassengerResponseDto>builder()
                .withValues(Collections.emptyList())
                .withCurrentPage(0)
                .withSize(DEFAULT_PAGEABLE_SIZE)
                .withLastPage(0)
                .withSort(UNSORTED);
    }
}
