package by.arvisit.cabapp.driverservice.validation;

import java.util.Map;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MapContainsKeyValidator implements ConstraintValidator<MapContainsKey, Map<String, ?>> {

    private String key;

    @Override
    public void initialize(MapContainsKey constraintAnnotation) {
        key = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Map<String, ?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.containsKey(key);
    }

}
