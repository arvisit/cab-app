package by.arvisit.cabapp.driverservice.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Map;

import org.springframework.stereotype.Component;

import by.arvisit.cabapp.driverservice.dto.DriversFilterParams;
import lombok.SneakyThrows;

@Component
public class DriversFilterParamsMapper {

    @SneakyThrows
    public DriversFilterParams fromMapParams(Map<String, String> params) {
        Constructor<?> constructor = DriversFilterParams.class.getDeclaredConstructors()[0];
        Parameter[] parameters = constructor.getParameters();

        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String value = params.get(parameter.getName());
            if (parameter.getType().equals(Boolean.class)) {
                args[i] = getNullOrBoolean(value);
            } else {
                args[i] = value;
            }
        }

        return (DriversFilterParams) constructor.newInstance(args);
    }

    private Boolean getNullOrBoolean(String str) {
        return str != null
                ? Boolean.parseBoolean(str)
                : null;
    }
}
