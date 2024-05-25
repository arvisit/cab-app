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

            spec = cb.and(spec, handleLikeStringParams(filterParams).toPredicate(root, query, cb));

            return spec;
        };
    }

    private Specification<Passenger> handleLikeStringParams(PassengersFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<Passenger, String>, String> strParams = new HashMap<>();
            strParams.put(Passenger_.email, filterParams.email());
            strParams.put(Passenger_.name, filterParams.name());

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
