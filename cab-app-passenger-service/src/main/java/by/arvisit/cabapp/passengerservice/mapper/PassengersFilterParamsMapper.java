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
            args[i] = params.get(parameters[i].getName());
        }

        return (PassengersFilterParams) constructor.newInstance(args);
    }
}
