package by.arvisit.cabapp.common.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import by.arvisit.cabapp.common.validation.provider.ParseableDateValuesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ParseableDateValuesValidator.class)
public @interface ParseableDateValues {

    Class<?> keysHolder();

    String message() default "{by.arvisit.cabapp.common.validation.ParseableDateValues.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
