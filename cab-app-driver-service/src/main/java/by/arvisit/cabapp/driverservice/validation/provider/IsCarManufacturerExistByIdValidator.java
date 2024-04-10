package by.arvisit.cabapp.driverservice.validation.provider;

import by.arvisit.cabapp.driverservice.service.CarManufacturerService;
import by.arvisit.cabapp.driverservice.validation.IsCarManufacturerExistById;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IsCarManufacturerExistByIdValidator implements ConstraintValidator<IsCarManufacturerExistById, Integer> {

    private final CarManufacturerService carManufacturerService;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return carManufacturerService.existsById(value);
    }

}
