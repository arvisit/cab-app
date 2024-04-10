package by.arvisit.cabapp.driverservice.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MapContainsKeyValidator.class)
public @interface MapContainsKey {

    String value();

    String message() default "{by.arvisit.cabapp.driverservice.validation.MapContainsKey.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
