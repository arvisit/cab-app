package by.arvisit.cabapp.driverservice.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarRequestDto;
import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.dto.DriversFilterParams;
import by.arvisit.cabapp.driverservice.persistence.model.Car;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;
import by.arvisit.cabapp.driverservice.persistence.model.Color;
import by.arvisit.cabapp.driverservice.persistence.model.Driver;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
    public static final String DEFAULT_PASSWORD = "password";
    public static final Boolean DEFAULT_IS_AVAILABLE = true;
    public static final Integer DEFAULT_PAGEABLE_SIZE = 10;
    public static final String UNSORTED = "UNSORTED";
    public static final String NEW_DRIVER_NAME = "John Doe";
    public static final String NEW_DRIVER_EMAIL = "john.doe@mail.com";
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String URL_COLORS = "/api/v1/colors";
    public static final String URL_CAR_MANUFACTURERS = "/api/v1/car-manufacturers";
    public static final String URL_CARS = "/api/v1/cars";
    public static final String URL_CARS_ID_TEMPLATE = "/api/v1/cars/{id}";
    public static final String URL_DRIVERS = "/api/v1/drivers";
    public static final String URL_AVAILABLE_DRIVERS = "/api/v1/drivers/available";
    public static final String URL_DRIVERS_ID_TEMPLATE = "/api/v1/drivers/{id}";
    public static final String URL_DRIVERS_ID_AVAILABILITY_TEMPLATE = "/api/v1/drivers/{id}/availability";
    public static final String URL_DRIVERS_EMAIL_TEMPLATE = "/api/v1/drivers/by-email/{email}";
    public static final String URL_DRIVERS_PARAM_VALUE_TEMPLATE = "/api/v1/drivers?{param}={value}";
    public static final String NOT_ALLOWED_REQUEST_PARAM = "driverId";
    public static final String NAME_REQUEST_PARAM = "name";
    public static final String IS_AVAILABLE_REQUEST_PARAM = "isAvailable";
    public static final String CAR_MANUFACTURER_NAME_REQUEST_PARAM = "carManufacturerName";
    public static final String EMAIL_REQUEST_PARAM = "email";
    public static final String NAME_TO_FILTER = "jack";
    public static final String EMAIL_TO_FILTER = "com";
    public static final String IS_AVAILABLE_TO_FILTER = "true";
    public static final String CAR_MANUFACTURER_NAME_TO_FILTER = "Audi";
    public static final String IS_AVAILABLE_KEY = "isAvailable";

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
                .withPassword(DEFAULT_PASSWORD)
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

    public static DriversFilterParams.DriversFilterParamsBuilder getEmptyDriversFilterParams() {
        return DriversFilterParams.builder()
                .withEmail(null)
                .withName(null)
                .withIsAvailable(null)
                .withCarManufacturerName(null);
    }

    public static DriversFilterParams.DriversFilterParamsBuilder getFilledDriversFilterParams() {
        return DriversFilterParams.builder()
                .withEmail(EMAIL_TO_FILTER)
                .withName(NAME_TO_FILTER)
                .withIsAvailable(Boolean.parseBoolean(IS_AVAILABLE_TO_FILTER))
                .withCarManufacturerName(CAR_MANUFACTURER_NAME_TO_FILTER);
    }

    public static Map<String, String> getRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put(NAME_REQUEST_PARAM, NAME_TO_FILTER);
        params.put(EMAIL_REQUEST_PARAM, EMAIL_TO_FILTER);
        params.put(IS_AVAILABLE_REQUEST_PARAM, IS_AVAILABLE_TO_FILTER);
        params.put(CAR_MANUFACTURER_NAME_REQUEST_PARAM, CAR_MANUFACTURER_NAME_TO_FILTER);
        return params;
    }

    public static Stream<String> blankStrings() {
        return Stream.of("", "   ", null);
    }

    public static Stream<String> malformedEmails() {
        return Stream.of("   ", "not-email", "not-email.mail.com", "not email", "not email@mail.com");
    }

    public static Stream<String> malformedCardNumbers() {
        return Stream.of("", "   ", "1234", "abc", "111122223333444a", "111122223333-444");
    }

    public static Stream<String> malformedCarRegistrationNumbers() {
        return Stream.of("", "   ", "1234", "abc", "111122223333444a", "111122223333-444", "A000AA-1", "0000YA-1",
                "0000AA1", "0000AA_1", "0000AA-0", "0000AA-9", "E000YA-1", "E000AA1", "E000AA_1", "E000AA-0",
                "E000AA-9");
    }

    public static Stream<String> malformedUUIDs() {
        return Stream.of("3abcc6a1-94da-4185-1-8a11c1b8efd2", "3abcc6a1-94da-4185-aaa1-8a11c1b8efdw",
                "3ABCC6A1-94DA-4185-AAA1-8A11C1B8EFD2", "   ", "1234", "abc", "111122223333444a",
                "111122223333-444");
    }

    public static Stream<String> malformedBooleans() {
        return Stream.of("", "3ABCC6A1-94DA-4185-AAA1-8A11C1B8EFD2", "   ", "1234", "abc", "111122223333444a",
                "111122223333-444", "truth", "lies", "trui", "falce");
    }
}
