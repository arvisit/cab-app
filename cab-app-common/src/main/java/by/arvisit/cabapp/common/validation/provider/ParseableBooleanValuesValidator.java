package by.arvisit.cabapp.common.validation.provider;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import by.arvisit.cabapp.common.validation.ParseableBooleanValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ParseableBooleanValuesValidator
        implements ConstraintValidator<ParseableBooleanValues, Map<String, String>> {

    private Set<String> keysForBoolean;

    @Override
    public void initialize(ParseableBooleanValues constraintAnnotation) {
        Class<?> keysHolder = constraintAnnotation.keysHolder();
        keysForBoolean = Arrays.stream(keysHolder.getDeclaredFields())
                .filter(f -> f.getType().equals(Boolean.class))
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        for (String key : keysForBoolean) {
            if (value.containsKey(key)) {
                if ("true".compareToIgnoreCase(value.get(key)) != 0
                        && "false".compareToIgnoreCase(value.get(key)) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
