package by.arvisit.cabapp.common.validation.provider;

import java.util.Map;
import java.util.Set;

import by.arvisit.cabapp.common.validation.MapContainsParseableBooleanValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MapContainsParseableBooleanValuesValidator
        implements ConstraintValidator<MapContainsParseableBooleanValues, Map<String, String>> {

    private Set<String> targetKeys;

    @Override
    public void initialize(MapContainsParseableBooleanValues constraintAnnotation) {
        targetKeys = Set.of(constraintAnnotation.keys());
    }

    @Override
    public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        for (String key : targetKeys) {
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
