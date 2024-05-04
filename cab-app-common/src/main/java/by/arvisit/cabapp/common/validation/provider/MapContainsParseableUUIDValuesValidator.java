package by.arvisit.cabapp.common.validation.provider;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import by.arvisit.cabapp.common.validation.MapContainsParseableUUIDValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MapContainsParseableUUIDValuesValidator implements ConstraintValidator<MapContainsParseableUUIDValues, Map<String, String>> {

    private Set<String> targetKeys;

    @Override
    public void initialize(MapContainsParseableUUIDValues constraintAnnotation) {
        targetKeys = Set.of(constraintAnnotation.keys());
    }

    @Override
    public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        for (String key : targetKeys) {
            if (value.containsKey(key)) {
                try {
                    String valueForKey = value.get(key);
                    UUID possibleUUID = UUID.fromString(valueForKey);
                    return possibleUUID.toString().equals(valueForKey);
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }
        }
        return true;
    }

}
