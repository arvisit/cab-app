package by.arvisit.cabapp.ridesservice.persistence.util;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.util.DateRange;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

@Component
public class RideSpecs {

    private static final Set<String> VALID_LIKE_STRING_PARAM_NAMES = Set.of("startAddress", "destinationAddress");
    private static final Set<String> VALID_EQUAL_STRING_PARAM_NAMES = Set.of("status", "paymentMethod");
    private static final Set<String> VALID_EQUAL_UUID_PARAM_NAMES = Set.of("passengerId", "driverId");
    private static final Set<String> VALID_DATE_PARAM_NAMES = Set.of("bookRide", "cancelRide", "acceptRide",
            "beginRide", "endRide", "finishRide");

    public Specification<Ride> getAllByFilter(Map<String, String> filterParams) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder cb = criteriaBuilder;
            Predicate spec = cb.conjunction();

            if (filterParams == null) {
                return spec;
            }

            for (Map.Entry<String, String> param : filterParams.entrySet()) {
                String paramKey = param.getKey();
                String paramValue = param.getValue();
                if (VALID_LIKE_STRING_PARAM_NAMES.contains(paramKey)) {
                    String likePattern = toLikePattern(paramValue);
                    spec = cb.and(spec, cb.like(cb.lower(root.get(paramKey)), likePattern));
                }
                if (VALID_EQUAL_STRING_PARAM_NAMES.contains(paramKey)) {
                    spec = cb.and(spec, cb.equal(root.get(paramKey), paramValue));
                }
                if (VALID_EQUAL_UUID_PARAM_NAMES.contains(paramKey)) {
                    spec = cb.and(spec, cb.equal(root.get(paramKey), UUID.fromString(paramValue)));
                }
                if (VALID_DATE_PARAM_NAMES.contains(paramKey)) {
                    DateRange dateRange = DateRange.fromSingleValue(paramValue);
                    spec = cb.and(spec,
                            cb.between(root.get(paramKey), dateRange.getStartDate(), dateRange.getEndDate()));
                }
            }
            return spec;
        };
    }

    private String toLikePattern(String str) {
        return "%" + str.toLowerCase() + "%";
    }
}
