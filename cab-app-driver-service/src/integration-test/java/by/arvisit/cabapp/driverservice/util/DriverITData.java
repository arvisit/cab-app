package by.arvisit.cabapp.driverservice.util;

import static by.arvisit.cabapp.driverservice.util.CarManufacturerITData.CAR_MANUFACTURER_RESPONSE_DTOS;
import static by.arvisit.cabapp.driverservice.util.ColorITData.COLOR_RESPONSE_DTOS;

import java.util.Collections;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.dto.CarRequestDto;
import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import by.arvisit.cabapp.driverservice.dto.DriverRequestDto;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;

public final class DriverITData {

    private DriverITData() {
    }

    public static final String URL_COLORS = "/api/v1/colors";
    public static final String URL_CAR_MANUFACTURERS = "/api/v1/car-manufacturers";
    public static final String URL_CARS = "/api/v1/cars";
    public static final String URL_CARS_ID_TEMPLATE = "/api/v1/cars/{id}";
    public static final String URL_DRIVERS = "/api/v1/drivers";
    public static final String URL_AVAILABLE_DRIVERS = "/api/v1/drivers/available";
    public static final String URL_DRIVERS_ID_TEMPLATE = "/api/v1/drivers/{id}";
    public static final String URL_DRIVERS_ID_AVAILABILITY_TEMPLATE = "/api/v1/drivers/{id}/availability";
    public static final String URL_DRIVERS_EMAIL_TEMPLATE = "/api/v1/drivers/by-email/{email}";

    public static final String JOHNNY_DOE_ID_STRING = "a7dd0543-0adc-4ea6-9ca7-7b72065ca011";
    public static final String JOHN_DOE_ID_STRING = "d1e04703-1af4-43ee-aae7-4ee06881ee59";
    public static final String JANNY_DOE_ID_STRING = "d80e3e08-5b82-4551-9428-bc5d45e6f3f6";
    public static final String JANE_DOE_ID_STRING = "ad4225da-7a89-4290-884f-a22701c83e94";

    public static final String JOHNNY_DOE_CAR_ID_STRING = "8805899c-8fcd-4585-8ccb-1edec54a964a";
    public static final String JOHN_DOE_CAR_ID_STRING = "6810330b-769e-4278-b050-f66371e2ee52";
    public static final String JANNY_DOE_CAR_ID_STRING = "08ca8ae6-f39c-4093-9b2a-3aa8fe57987f";
    public static final String JANE_DOE_CAR_ID_STRING = "fd937a51-12ea-47aa-bb1a-68289a50111c";

    public static final String JOHN_DOE_EMAIL = "john.doe@mail.com";

    public static final String NEW_DRIVER_NAME = "Jack Black";
    public static final String NEW_DRIVER_EMAIL = "jack.black@mail.com";
    public static final String NEW_DRIVER_CARD_NUMBER = "0000111122223333";

    public static final String NEW_CAR_REGISTRATION_NUMBER = "0000AA-1";

    public static final int DEFAULT_PAGEABLE_SIZE = 10;
    public static final String UNSORTED = "UNSORTED";
    public static final int SHIFT = 1;

    public static DriverRequestDto.DriverRequestDtoBuilder getSaveDriverRequest() {
        return DriverRequestDto.builder()
                .withName(NEW_DRIVER_NAME)
                .withEmail(NEW_DRIVER_EMAIL)
                .withCardNumber(NEW_DRIVER_CARD_NUMBER)
                .withCar(getNewCarRequest().build());
    }

    public static CarRequestDto.CarRequestDtoBuilder getNewCarRequest() {
        return CarRequestDto.builder()
                .withColorId(1 - SHIFT)
                .withManufacturerId(1 - SHIFT)
                .withRegistrationNumber(NEW_CAR_REGISTRATION_NUMBER);
    }

    public static DriverResponseDto.DriverResponseDtoBuilder getAddedDriverResponse() {
        return DriverResponseDto.builder()
                .withId(null)
                .withName(NEW_DRIVER_NAME)
                .withEmail(NEW_DRIVER_EMAIL)
                .withCardNumber(NEW_DRIVER_CARD_NUMBER)
                .withCar(getNewCarResponse().build())
                .withIsAvailable(true);
    }

    public static CarResponseDto.CarResponseDtoBuilder getNewCarResponse() {
        return CarResponseDto.builder()
                .withId(null)
                .withColor(COLOR_RESPONSE_DTOS.get(1))
                .withManufacturer(CAR_MANUFACTURER_RESPONSE_DTOS.get(1))
                .withRegistrationNumber(NEW_CAR_REGISTRATION_NUMBER);
    }

    public static DriverRequestDto.DriverRequestDtoBuilder getUpdateDriverRequest() {
        return DriverRequestDto.builder()
                .withName(NEW_DRIVER_NAME)
                .withEmail(NEW_DRIVER_EMAIL)
                .withCardNumber(NEW_DRIVER_CARD_NUMBER)
                .withCar(getNewCarRequest().build());
    }

    public static DriverResponseDto.DriverResponseDtoBuilder getUpdatedDriverResponse() {
        return DriverResponseDto.builder()
                .withId(JOHN_DOE_ID_STRING)
                .withName(NEW_DRIVER_NAME)
                .withEmail(NEW_DRIVER_EMAIL)
                .withCardNumber(NEW_DRIVER_CARD_NUMBER)
                .withCar(getNewCarResponse().build())
                .withIsAvailable(true);
    }

    public static DriverResponseDto.DriverResponseDtoBuilder getJohnnyDoe() {
        return DriverResponseDto.builder()
                .withId(JOHNNY_DOE_ID_STRING)
                .withName("Johnny Doe")
                .withEmail("johnny.doe@yahoo.de")
                .withCardNumber("1522613953683617")
                .withCar(getJohnnyDoeCar().build())
                .withIsAvailable(true);
    }

    public static CarResponseDto.CarResponseDtoBuilder getJohnnyDoeCar() {
        return CarResponseDto.builder()
                .withId(JOHNNY_DOE_CAR_ID_STRING)
                .withManufacturer(CAR_MANUFACTURER_RESPONSE_DTOS.get(9 - SHIFT))
                .withColor(COLOR_RESPONSE_DTOS.get(7 - SHIFT))
                .withRegistrationNumber("E070CC-4");
    }

    public static DriverResponseDto.DriverResponseDtoBuilder getJohnDoe() {
        return DriverResponseDto.builder()
                .withId(JOHN_DOE_ID_STRING)
                .withName("John Doe")
                .withEmail("john.doe@mail.com")
                .withCardNumber("3917881684449050")
                .withCar(getJohnDoeCar().build())
                .withIsAvailable(false);
    }

    public static CarResponseDto.CarResponseDtoBuilder getJohnDoeCar() {
        return CarResponseDto.builder()
                .withId(JOHN_DOE_CAR_ID_STRING)
                .withManufacturer(CAR_MANUFACTURER_RESPONSE_DTOS.get(1 - SHIFT))
                .withColor(COLOR_RESPONSE_DTOS.get(2 - SHIFT))
                .withRegistrationNumber("E635TO-6");
    }

    public static DriverResponseDto.DriverResponseDtoBuilder getJannyDoe() {
        return DriverResponseDto.builder()
                .withId(JANNY_DOE_ID_STRING)
                .withName("Janny Doe")
                .withEmail("janny.doe@yahoo.com.br")
                .withCardNumber("9239176603428452")
                .withCar(getJannyDoeCar().build())
                .withIsAvailable(false);
    }

    public static CarResponseDto.CarResponseDtoBuilder getJannyDoeCar() {
        return CarResponseDto.builder()
                .withId(JANNY_DOE_CAR_ID_STRING)
                .withManufacturer(CAR_MANUFACTURER_RESPONSE_DTOS.get(7 - SHIFT))
                .withColor(COLOR_RESPONSE_DTOS.get(1 - SHIFT))
                .withRegistrationNumber("E472XH-5");
    }

    public static DriverResponseDto.DriverResponseDtoBuilder getJaneDoe() {
        return DriverResponseDto.builder()
                .withId(JANE_DOE_ID_STRING)
                .withName("Jane Doe")
                .withEmail("jane.doe@yahoo.com.ar")
                .withCardNumber("9902232414679759")
                .withCar(getJaneDoeCar().build())
                .withIsAvailable(false);
    }

    public static CarResponseDto.CarResponseDtoBuilder getJaneDoeCar() {
        return CarResponseDto.builder()
                .withId(JANE_DOE_CAR_ID_STRING)
                .withManufacturer(CAR_MANUFACTURER_RESPONSE_DTOS.get(3 - SHIFT))
                .withColor(COLOR_RESPONSE_DTOS.get(2 - SHIFT))
                .withRegistrationNumber("E391MP-4");
    }

    public static ListContainerResponseDto.ListContainerResponseDtoBuilder<DriverResponseDto> getListContainerForDrivers() {
        return ListContainerResponseDto.<DriverResponseDto>builder()
                .withValues(Collections.emptyList())
                .withCurrentPage(0)
                .withSize(DEFAULT_PAGEABLE_SIZE)
                .withLastPage(0)
                .withSort(UNSORTED);
    }
}
