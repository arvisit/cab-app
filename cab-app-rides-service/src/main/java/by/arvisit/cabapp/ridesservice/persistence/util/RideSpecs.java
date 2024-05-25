package by.arvisit.cabapp.ridesservice.persistence.util;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.util.DateRange;
import by.arvisit.cabapp.common.util.SpecUtil;
import by.arvisit.cabapp.ridesservice.dto.RidesFilterParams;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import by.arvisit.cabapp.ridesservice.persistence.model.Ride_;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.SingularAttribute;

@Component
public class RideSpecs {

    public Specification<Ride> getAllByFilter(RidesFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            if (filterParams == null) {
                return spec;
            }

            spec = cb.and(spec, handleLikeStringParams(filterParams).toPredicate(root, query, cb));
            spec = cb.and(spec, handleEqualEnumParams(filterParams).toPredicate(root, query, cb));
            spec = cb.and(spec, handleEqualUUIDParams(filterParams).toPredicate(root, query, cb));
            spec = cb.and(spec, handleBetweenDateRangeParams(filterParams).toPredicate(root, query, cb));

            return spec;
        };
    }

    private Specification<Ride> handleLikeStringParams(RidesFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<Ride, String>, String> strParams = new HashMap<>();
            strParams.put(Ride_.startAddress, filterParams.startAddress());
            strParams.put(Ride_.destinationAddress, filterParams.destinationAddress());

            for (Map.Entry<SingularAttribute<Ride, String>, String> param : strParams.entrySet()) {
                SingularAttribute<Ride, String> key = param.getKey();
                String value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.likeString(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }

    private Specification<Ride> handleEqualEnumParams(RidesFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<Ride, ? extends Enum<?>>, String> enumParams = new HashMap<>();
            enumParams.put(Ride_.status, filterParams.status());
            enumParams.put(Ride_.paymentMethod, filterParams.paymentMethod());

            for (Map.Entry<SingularAttribute<Ride, ? extends Enum<?>>, String> param : enumParams.entrySet()) {
                SingularAttribute<Ride, ? extends Enum<?>> key = param.getKey();
                String value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.equalEnum(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }

    private Specification<Ride> handleEqualUUIDParams(RidesFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<Ride, UUID>, UUID> uuidParams = new HashMap<>();
            uuidParams.put(Ride_.passengerId, filterParams.passengerId());
            uuidParams.put(Ride_.driverId, filterParams.driverId());

            for (Map.Entry<SingularAttribute<Ride, UUID>, UUID> param : uuidParams.entrySet()) {
                SingularAttribute<Ride, UUID> key = param.getKey();
                UUID value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.equalUUID(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }

    private Specification<Ride> handleBetweenDateRangeParams(RidesFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<Ride, ZonedDateTime>, DateRange> dateParams = new HashMap<>();
            dateParams.put(Ride_.bookRide, filterParams.bookRide());
            dateParams.put(Ride_.cancelRide, filterParams.cancelRide());
            dateParams.put(Ride_.acceptRide, filterParams.acceptRide());
            dateParams.put(Ride_.beginRide, filterParams.beginRide());
            dateParams.put(Ride_.endRide, filterParams.endRide());
            dateParams.put(Ride_.finishRide, filterParams.finishRide());

            for (Map.Entry<SingularAttribute<Ride, ZonedDateTime>, DateRange> param : dateParams.entrySet()) {
                SingularAttribute<Ride, ZonedDateTime> key = param.getKey();
                DateRange value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.betweenDateRange(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }
}
