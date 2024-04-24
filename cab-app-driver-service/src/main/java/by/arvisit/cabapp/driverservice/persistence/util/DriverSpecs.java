package by.arvisit.cabapp.driverservice.persistence.util;

import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import by.arvisit.cabapp.driverservice.persistence.model.Car;
import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;
import by.arvisit.cabapp.driverservice.persistence.model.Driver;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Component
public class DriverSpecs {

    private static final String CAR_MANUFACTURER_NAME_PARAM_NAME = "carManufacturerName";
    private static final Set<String> VALID_DIRECT_STRING_PARAM_NAMES = Set.of("name", "email");
    private static final Set<String> VALID_DIRECT_BOOLEAN_PARAM_NAMES = Set.of("isAvailable");

    public Specification<Driver> getAllByFilter(Map<String, String> filterParams) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder cb = criteriaBuilder;
            Predicate spec = cb.conjunction();
            String carManufacturerNameValue = filterParams.get(CAR_MANUFACTURER_NAME_PARAM_NAME);
            spec = cb.and(spec, getAllByCarManufacturerName(carManufacturerNameValue)
                    .toPredicate(root, query, criteriaBuilder));
            if (filterParams != null) {
                for (Map.Entry<String, String> param : filterParams.entrySet()) {
                    String paramKey = param.getKey();
                    String paramValue = param.getValue();
                    if (VALID_DIRECT_STRING_PARAM_NAMES.contains(paramKey) && !paramValue.trim().isEmpty()) {
                        String likePattern = toLikePattern(paramValue);
                        spec = cb.and(spec, cb.like(cb.lower(root.get(paramKey)), likePattern));
                    }
                    if (VALID_DIRECT_BOOLEAN_PARAM_NAMES.contains(paramKey) && !paramValue.trim().isEmpty()) {
                        Boolean parsedValue = Boolean.valueOf(paramValue);
                        spec = cb.and(spec, cb.equal(root.get(paramKey), parsedValue));
                    }
                }
            }
            return spec;
        };
    }

    private Specification<Driver> getAllByCarManufacturerName(String carManufacturerName) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder cb = criteriaBuilder;
            Predicate spec = cb.conjunction();
            if (carManufacturerName != null && !carManufacturerName.trim().isEmpty()) {
                Join<Driver, Car> joinCar = root.join("car");
                Join<Car, CarManufacturer> joinCarManufacturer = joinCar.join("manufacturer");
                String likePattern = toLikePattern(carManufacturerName);
                spec = cb.and(spec, cb.like(cb.lower(joinCarManufacturer.get("name")), likePattern));
            }
            return spec;
        };
    }

    private String toLikePattern(String str) {
        return "%" + str.toLowerCase() + "%";
    }

}
