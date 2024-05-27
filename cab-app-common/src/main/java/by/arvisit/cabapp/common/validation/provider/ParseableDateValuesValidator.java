package by.arvisit.cabapp.common.validation.provider;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import by.arvisit.cabapp.common.util.DateRange;
import by.arvisit.cabapp.common.util.ValidationRegexp;
import by.arvisit.cabapp.common.validation.ParseableDateValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ParseableDateValuesValidator
        implements ConstraintValidator<ParseableDateValues, Map<String, String>> {

    private Set<String> targetKeys;

    @Override
    public void initialize(ParseableDateValues constraintAnnotation) {
        Class<?> keysHolder = constraintAnnotation.keysHolder();
        targetKeys = Arrays.stream(keysHolder.getDeclaredFields())
                .filter(f -> f.getType().equals(DateRange.class))
                .map(Field::getName)
                .collect(Collectors.toSet());
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
