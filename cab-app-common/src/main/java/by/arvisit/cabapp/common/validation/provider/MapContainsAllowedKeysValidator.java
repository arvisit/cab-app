package by.arvisit.cabapp.common.validation.provider;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import by.arvisit.cabapp.common.validation.MapContainsAllowedKeys;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MapContainsAllowedKeysValidator implements ConstraintValidator<MapContainsAllowedKeys, Map<String, ?>> {

    private Set<String> allowedKeys;

    @Override
    public void initialize(MapContainsAllowedKeys constraintAnnotation) {
        allowedKeys = Set.of(constraintAnnotation.keys());
    }

    @Override
    public boolean isValid(Map<String, ?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Set<String> keys = value.keySet();
        Set<String> notAllowedKeys = keys.stream()
                .filter(k -> !allowedKeys.contains(k))
                .collect(Collectors.toSet());
        return notAllowedKeys.isEmpty();
    }

}
