package by.arvisit.cabapp.driverservice.util;

import java.util.List;

import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CarManufacturerIntegrationTestData {

    public static final List<CarManufacturerResponseDto> CAR_MANUFACTURERS;

    static {
        CAR_MANUFACTURERS = List.of(new CarManufacturerResponseDto(1, "Toyota"),
                new CarManufacturerResponseDto(2, "Volkswagen"),
                new CarManufacturerResponseDto(3, "Ford"),
                new CarManufacturerResponseDto(4, "Chevrolet"),
                new CarManufacturerResponseDto(5, "Nissan"),
                new CarManufacturerResponseDto(6, "Honda"),
                new CarManufacturerResponseDto(7, "Hyundai"),
                new CarManufacturerResponseDto(8, "BMW"),
                new CarManufacturerResponseDto(9, "Mercedes-Benz"),
                new CarManufacturerResponseDto(10, "Audi"));
    }
}
