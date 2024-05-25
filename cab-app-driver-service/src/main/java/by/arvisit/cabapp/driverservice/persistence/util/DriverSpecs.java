package by.arvisit.cabapp.driverservice.persistence.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.util.SpecUtil;
import by.arvisit.cabapp.driverservice.dto.DriversFilterParams;
import by.arvisit.cabapp.driverservice.persistence.model.Car;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer_;
import by.arvisit.cabapp.driverservice.persistence.model.Car_;
import by.arvisit.cabapp.driverservice.persistence.model.Driver;
import by.arvisit.cabapp.driverservice.persistence.model.Driver_;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.SingularAttribute;

@Component
public class DriverSpecs {

    public Specification<Driver> getAllByFilter(DriversFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            if (filterParams == null) {
                return spec;
            }

            spec = (filterParams.carManufacturerName() != null)
                    ? cb.and(spec, getAllByCarManufacturerName(filterParams).toPredicate(root, query, cb))
                    : spec;

            spec = cb.and(spec, handleLikeStringParams(filterParams).toPredicate(root, query, cb));
            spec = cb.and(spec, handleEqualBooleanParams(filterParams).toPredicate(root, query, cb));

            return spec;
        };
    }

    private Specification<Driver> handleLikeStringParams(DriversFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<Driver, String>, String> strParams = new HashMap<>();
            strParams.put(Driver_.email, filterParams.email());
            strParams.put(Driver_.name, filterParams.name());

            for (Map.Entry<SingularAttribute<Driver, String>, String> param : strParams.entrySet()) {
                SingularAttribute<Driver, String> key = param.getKey();
                String value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.likeString(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }

    private Specification<Driver> handleEqualBooleanParams(DriversFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<Driver, Boolean>, Boolean> strParams = new HashMap<>();
            strParams.put(Driver_.isAvailable, filterParams.isAvailable());

            for (Map.Entry<SingularAttribute<Driver, Boolean>, Boolean> param : strParams.entrySet()) {
                SingularAttribute<Driver, Boolean> key = param.getKey();
                Boolean value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.equalBoolean(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }

    private Specification<Driver> getAllByCarManufacturerName(DriversFilterParams filterParams) {
        String carManufacturerName = filterParams.carManufacturerName();
        return (root, query, cb) -> {
            Join<Driver, Car> joinCar = root.join(Driver_.car);
            Join<Car, CarManufacturer> joinCarManufacturer = joinCar.join(Car_.manufacturer);
            String likePattern = SpecUtil.toLikePattern(carManufacturerName);
            return cb.like(cb.lower(joinCarManufacturer.get(CarManufacturer_.name)), likePattern);
        };
    }
}
