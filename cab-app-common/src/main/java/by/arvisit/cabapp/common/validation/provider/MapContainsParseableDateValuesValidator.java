package by.arvisit.cabapp.common.validation.provider;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.arvisit.cabapp.common.util.ValidationRegexp;
import by.arvisit.cabapp.common.validation.MapContainsParseableDateValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MapContainsParseableDateValuesValidator
        implements ConstraintValidator<MapContainsParseableDateValues, Map<String, String>> {

    private Set<String> targetKeys;

    @Override
    public void initialize(MapContainsParseableDateValues constraintAnnotation) {
        targetKeys = Set.of(constraintAnnotation.keys());
    }

    @Override
    public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Pattern pattern = ValidationRegexp.DATE_AS_FILTER_PARAM_VALIDATION_COMPILED;
        for (String key : targetKeys) {
            if (value.containsKey(key)) {
                Matcher matcher = pattern.matcher(value.get(key));
                if (!matcher.matches()) {
                    return false;
                }
            }
        }
        return true;
    }

}
