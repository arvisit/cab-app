package by.arvisit.cabapp.passengerservice.persistence.util;

import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

public class PassengerSpecs {

    private static final Set<String> VALID_PARAM_NAMES = Set.of("name", "email");

    public static Specification<Passenger> getAllByFilter(Map<String, String> filterParams) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder cb = criteriaBuilder;
            Predicate spec = cb.conjunction();
            if (filterParams != null) {
                for (Map.Entry<String, String> param : filterParams.entrySet()) {
                    String paramKey = param.getKey();
                    String paramValue = param.getValue();
                    if (VALID_PARAM_NAMES.contains(paramKey) && !paramValue.trim().isEmpty()) {
                        String likePattern = toLikePattern(paramValue);
                        spec = cb.and(spec, cb.like(cb.lower(root.get(paramKey)), likePattern));
                    }
                }
            }
            return spec;
        };
    }

    private static String toLikePattern(String str) {
        return "%" + str.toLowerCase() + "%";
    }

}
