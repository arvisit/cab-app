package by.arvisit.cabapp.ridesservice.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.util.DateRange;
import by.arvisit.cabapp.ridesservice.dto.RidesFilterParams;
import lombok.SneakyThrows;

@Component
public class RidesFilterParamsMapper {

    @SneakyThrows
    public RidesFilterParams fromMapParams(Map<String, String> params) {
        Constructor<?> constructor = RidesFilterParams.class.getDeclaredConstructors()[0];
        Parameter[] parameters = constructor.getParameters();

        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String value = params.get(parameter.getName());
            if (parameter.getType().equals(UUID.class)) {
                args[i] = getNullOrUUID(value);
            } else if (parameter.getType().equals(DateRange.class)) {
                args[i] = getNullOrDateRange(value);
            } else if (parameter.getType().equals(Integer.class)) {
                args[i] = getNullOrInteger(value);
            } else {
                args[i] = value;
            }
        }

        return (RidesFilterParams) constructor.newInstance(args);
    }

    private UUID getNullOrUUID(String str) {
        return str != null
                ? UUID.fromString(str)
                : null;
    }

    private DateRange getNullOrDateRange(String str) {
        return str != null
                ? DateRange.fromSingleValue(str)
                : null;
    }

    private Integer getNullOrInteger(String str) {
        return str != null
                ? Integer.parseInt(str)
                : null;
    }
}
