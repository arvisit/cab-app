package by.arvisit.cabapp.driverservice.util;

import java.util.List;
import java.util.UUID;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarRequestDto;
import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Car;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;
import by.arvisit.cabapp.driverservice.persistence.model.Color;
import by.arvisit.cabapp.driverservice.persistence.model.Driver;

public final class DriverTestData {

    public static final Integer COLOR_ID_WHITE = 1;
    public static final String COLOR_NAME_WHITE = "White";
    public static final Integer COLOR_ID_BLACK = 2;
    public static final String COLOR_NAME_BLACK = "Black";
    public static final Integer DEFAULT_CAR_MANUFACTURER_ID = 1;
    public static final String DEFAULT_CAR_MANUFACTURER_NAME = "Toyota";
    public static final String DEFAULT_CAR_ID_STRING = "8805899c-8fcd-4585-8ccb-1edec54a964a";
    public static final UUID DEFAULT_CAR_ID_UUID = UUID.fromString(DEFAULT_CAR_ID_STRING);
    public static final String DEFAULT_CAR_REGISTRATION_NUMBER = "E070CC-4";
    public static final String DEFAULT_DRIVER_ID_STRING = "a7dd0543-0adc-4ea6-9ca7-7b72065ca011";
    public static final UUID DEFAULT_DRIVER_ID_UUID = UUID.fromString(DEFAULT_DRIVER_ID_STRING);
    public static final String DEFAULT_DRIVER_NAME = "Jeremias Olsen";
    public static final String DEFAULT_DRIVER_EMAIL = "jeremias.olsen@frontiernet.net";
    public static final String DEFAULT_DRIVER_CARD_NUMBER = "1522613953683617";
    public static final Boolean DEFAULT_IS_AVAILABLE = true;
    public static final Integer DEFAULT_PAGEABLE_SIZE = 10;
    public static final String UNSORTED = "UNSORTED";
    public static final String NEW_DRIVER_NAME = "John Doe";
    public static final String NEW_DRIVER_EMAIL = "john.doe@mail.com";

    private DriverTestData() {
    }

    public static Color.ColorBuilder getColor() {
        return Color.builder()
                .withId(COLOR_ID_WHITE)
                .withName(COLOR_NAME_WHITE);
    }

    public static ColorResponseDto.ColorResponseDtoBuilder getColorResponseDto() {
        return ColorResponseDto.builder()
                .withId(COLOR_ID_WHITE)
                .withName(COLOR_NAME_WHITE);
    }

    public static CarManufacturer.CarManufacturerBuilder getCarManufacturer() {
        return CarManufacturer.builder()
                .withId(DEFAULT_CAR_MANUFACTURER_ID)
                .withName(DEFAULT_CAR_MANUFACTURER_NAME);
    }

    public static CarManufacturerResponseDto.CarManufacturerResponseDtoBuilder getCarManufacturerResponseDto() {
        return CarManufacturerResponseDto.builder()
                .withId(DEFAULT_CAR_MANUFACTURER_ID)
                .withName(DEFAULT_CAR_MANUFACTURER_NAME);
    }

    public static Car.CarBuilder getCar() {
        return Car.builder()
                .withId(DEFAULT_CAR_ID_UUID)
                .withRegistrationNumber(DEFAULT_CAR_REGISTRATION_NUMBER)
                .withColor(getColor().build())
                .withManufacturer(getCarManufacturer().build());
    }

    public static CarRequestDto.CarRequestDtoBuilder getCarRequestDto() {
        return CarRequestDto.builder()
                .withManufacturerId(DEFAULT_CAR_MANUFACTURER_ID)
                .withColorId(COLOR_ID_WHITE)
                .withRegistrationNumber(DEFAULT_CAR_REGISTRATION_NUMBER);
    }

    public static CarResponseDto.CarResponseDtoBuilder getCarResponseDto() {
        return CarResponseDto.builder()
                .withId(DEFAULT_CAR_ID_STRING)
                .withManufacturer(getCarManufacturerResponseDto().build())
                .withColor(getColorResponseDto().build())
                .withRegistrationNumber(DEFAULT_CAR_REGISTRATION_NUMBER);
    }

    public static Driver.DriverBuilder getDriver() {
        return Driver.builder()
                .withId(DEFAULT_DRIVER_ID_UUID)
                .withName(DEFAULT_DRIVER_NAME)
                .withEmail(DEFAULT_DRIVER_EMAIL)
                .withCardNumber(DEFAULT_DRIVER_CARD_NUMBER)
                .withCar(getCar().build())
                .withIsAvailable(DEFAULT_IS_AVAILABLE);
    }

    public static DriverRequestDto.DriverRequestDtoBuilder getDriverRequestDto() {
        return DriverRequestDto.builder()
                .withName(DEFAULT_DRIVER_NAME)
                .withEmail(DEFAULT_DRIVER_EMAIL)
                .withCardNumber(DEFAULT_DRIVER_CARD_NUMBER)
                .withCar(getCarRequestDto().build());
    }

    public static DriverResponseDto.DriverResponseDtoBuilder getDriverResponseDto() {
        return DriverResponseDto.builder()
                .withId(DEFAULT_DRIVER_ID_STRING)
                .withName(DEFAULT_DRIVER_NAME)
                .withEmail(DEFAULT_DRIVER_EMAIL)
                .withCardNumber(DEFAULT_DRIVER_CARD_NUMBER)
                .withCar(getCarResponseDto().build())
                .withIsAvailable(DEFAULT_IS_AVAILABLE);
    }

    public static ListContainerResponseDto.ListContainerResponseDtoBuilder<DriverResponseDto> getDriverResponseDtoInListContainer() {
        return ListContainerResponseDto.<DriverResponseDto>builder()
                .withCurrentPage(0)
                .withSize(DEFAULT_PAGEABLE_SIZE)
                .withLastPage(0)
                .withSort(UNSORTED)
                .withValues(List.of(getDriverResponseDto().build()));
    }

    public static ListContainerResponseDto.ListContainerResponseDtoBuilder<ColorResponseDto> getColorResponseDtoInListContainer() {
        return ListContainerResponseDto.<ColorResponseDto>builder()
                .withCurrentPage(0)
                .withSize(DEFAULT_PAGEABLE_SIZE)
                .withLastPage(0)
                .withSort(UNSORTED)
                .withValues(List.of(getColorResponseDto().build()));
    }

    public static ListContainerResponseDto.ListContainerResponseDtoBuilder<CarManufacturerResponseDto> getCarManufacturerResponseDtoInListContainer() {
        return ListContainerResponseDto.<CarManufacturerResponseDto>builder()
                .withCurrentPage(0)
                .withSize(DEFAULT_PAGEABLE_SIZE)
                .withLastPage(0)
                .withSort(UNSORTED)
                .withValues(List.of(getCarManufacturerResponseDto().build()));
    }

    public static ListContainerResponseDto.ListContainerResponseDtoBuilder<CarResponseDto> getCarResponseDtoInListContainer() {
        return ListContainerResponseDto.<CarResponseDto>builder()
                .withCurrentPage(0)
                .withSize(DEFAULT_PAGEABLE_SIZE)
                .withLastPage(0)
                .withSort(UNSORTED)
                .withValues(List.of(getCarResponseDto().build()));
    }
}
