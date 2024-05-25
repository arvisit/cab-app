package by.arvisit.cabapp.driverservice.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

import by.arvisit.cabapp.driverservice.dto.DriversFilterParams;

@Component
public class DriversFilterParamsMapper {

    private static final String IS_AVAILABLE = "isAvailable";
    private static final String CAR_MANUFACTURER_NAME = "carManufacturerName";
    private static final String EMAIL = "email";
    private static final String NAME = "name";

    public DriversFilterParams fromMapParams(Map<String, String> params) {
        String isAvailable = params.get(IS_AVAILABLE);
        return DriversFilterParams.builder()
                .withName(params.get(NAME))
                .withEmail(params.get(EMAIL))
                .withCarManufacturerName(params.get(CAR_MANUFACTURER_NAME))
                .withIsAvailable(isAvailable != null
                        ? Boolean.parseBoolean(isAvailable)
                        : null)
                .build();
    }
}
