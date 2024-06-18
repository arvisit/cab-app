package by.arvisit.cabapp.passengerservice.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Map;

import org.springframework.stereotype.Component;

import by.arvisit.cabapp.passengerservice.dto.PassengersFilterParams;
import lombok.SneakyThrows;

@Component
public class PassengersFilterParamsMapper {

    @SneakyThrows
    public PassengersFilterParams fromMapParams(Map<String, String> params) {
        Constructor<?> constructor = PassengersFilterParams.class.getDeclaredConstructors()[0];
        Parameter[] parameters = constructor.getParameters();

        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String value = params.get(parameter.getName());
            if (parameter.getType().equals(Integer.class)) {
                args[i] = getNullOrInteger(value);
            } else {
                args[i] = value;
            }
        }

        return (PassengersFilterParams) constructor.newInstance(args);
    }

    private Integer getNullOrInteger(String str) {
        return str != null
                ? Integer.parseInt(str)
                : null;
    }
}
