package by.arvisit.cabapp.common.validation.provider;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import by.arvisit.cabapp.common.validation.AllowedKeys;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AllowedKeysValidator implements ConstraintValidator<AllowedKeys, Map<String, ?>> {

    private Set<String> allowedKeys;

    @Override
    public void initialize(AllowedKeys constraintAnnotation) {
        Class<?> keysHolder = constraintAnnotation.keysHolder();
        allowedKeys = Arrays.stream(keysHolder.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
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

        if (!notAllowedKeys.isEmpty()) {
            String notAllowedKeysString = notAllowedKeys.stream().collect(Collectors.joining(", "));
            String allowedKeysString = allowedKeys.stream().collect(Collectors.joining(", "));
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Invalid input parameters: " + notAllowedKeysString + ". Valid parameters are: "
                            + allowedKeysString)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
