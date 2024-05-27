package by.arvisit.cabapp.passengerservice.persistence.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.util.SpecUtil;
import by.arvisit.cabapp.passengerservice.dto.PassengersFilterParams;
import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;
import by.arvisit.cabapp.passengerservice.persistence.model.Passenger_;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.SingularAttribute;

@Component
public class PassengerSpecs {

    public Specification<Passenger> getAllByFilter(PassengersFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            if (filterParams == null) {
                return spec;
            }

            Map<SingularAttribute<Passenger, String>, String> strParams = collectLikeStringParams(filterParams);
            spec = cb.and(spec, handleLikeStringParams(strParams).toPredicate(root, query, cb));

            return spec;
        };
    }

    private Map<SingularAttribute<Passenger, String>, String> collectLikeStringParams(
            PassengersFilterParams filterParams) {
        Map<SingularAttribute<Passenger, String>, String> params = new HashMap<>();
        params.put(Passenger_.email, filterParams.email());
        params.put(Passenger_.name, filterParams.name());
        return params;
    }

    private Specification<Passenger> handleLikeStringParams(
            Map<SingularAttribute<Passenger, String>, String> strParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();

            for (Map.Entry<SingularAttribute<Passenger, String>, String> param : strParams.entrySet()) {
                SingularAttribute<Passenger, String> key = param.getKey();
                String value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.likeString(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }
}
