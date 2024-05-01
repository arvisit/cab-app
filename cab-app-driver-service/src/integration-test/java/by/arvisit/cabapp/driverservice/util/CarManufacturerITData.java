package by.arvisit.cabapp.driverservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import by.arvisit.cabapp.driverservice.dto.CarManufacturerResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;

public class CarManufacturerITData {

    private CarManufacturerITData() {
    }

    private static final Map<Integer, String> CAR_MANUFACTURER_DATA;
    public static final List<CarManufacturer> CAR_MANUFACTURERS = new ArrayList<>();
    public static final List<CarManufacturerResponseDto> CAR_MANUFACTURER_RESPONSE_DTOS = new ArrayList<>();

    static {
        CAR_MANUFACTURER_DATA = Map.of(1, "Toyota", 2, "Volkswagen", 3, "Ford", 4, "Chevrolet", 5, "Nissan", 6, "Honda",
                7, "Hyundai", 8, "BMW", 9, "Mercedes-Benz", 10, "Audi");

        for (Map.Entry<Integer, String> entry : CAR_MANUFACTURER_DATA.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            CAR_MANUFACTURERS.add(new CarManufacturer(key, value));
            CAR_MANUFACTURER_RESPONSE_DTOS.add(new CarManufacturerResponseDto(key, value));
        }
        CAR_MANUFACTURERS.sort((i1, i2) -> {
            if (i1.getId() < i2.getId()) {
                return -1;
            } else if (i1.getId() > i2.getId()) {
                return 1;
            } else {
                return 0;
            }
        });
        CAR_MANUFACTURER_RESPONSE_DTOS.sort((i1, i2) -> {
            if (i1.id() < i2.id()) {
                return -1;
            } else if (i1.id() > i2.id()) {
                return 1;
            } else {
                return 0;
            }
        });

    }
}
