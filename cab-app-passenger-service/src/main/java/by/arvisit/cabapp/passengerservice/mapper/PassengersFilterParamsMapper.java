package by.arvisit.cabapp.passengerservice.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

import by.arvisit.cabapp.passengerservice.dto.PassengersFilterParams;

@Component
public class PassengersFilterParamsMapper {

    private static final String EMAIL = "email";
    private static final String NAME = "name";

    public PassengersFilterParams fromMapParams(Map<String, String> params) {
        return PassengersFilterParams.builder()
                .withName(params.get(NAME))
                .withEmail(params.get(EMAIL))
                .build();
    }
}
