package by.arvisit.cabapp.driverservice.validation.provider;

import by.arvisit.cabapp.driverservice.service.ColorService;
import by.arvisit.cabapp.driverservice.validation.IsColorExistById;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IsColorExistByIdValidator implements ConstraintValidator<IsColorExistById, Integer> {

    private final ColorService colorService;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return colorService.existsById(value);
    }

}
