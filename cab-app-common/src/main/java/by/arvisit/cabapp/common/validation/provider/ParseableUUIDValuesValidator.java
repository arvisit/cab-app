package by.arvisit.cabapp.common.validation.provider;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import by.arvisit.cabapp.common.validation.ParseableUUIDValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ParseableUUIDValuesValidator implements ConstraintValidator<ParseableUUIDValues, Map<String, String>> {

    private Set<String> targetKeys;

    @Override
    public void initialize(ParseableUUIDValues constraintAnnotation) {
        Class<?> keysHolder = constraintAnnotation.keysHolder();
        targetKeys = Arrays.stream(keysHolder.getDeclaredFields())
                .filter(f -> f.getType().equals(UUID.class))
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        for (String key : targetKeys) {
            if (value.containsKey(key)) {
                try {
                    UUID.fromString(value.get(key));
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
        }
        return true;
    }

}
