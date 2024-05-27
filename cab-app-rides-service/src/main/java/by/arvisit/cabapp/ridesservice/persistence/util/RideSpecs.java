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

            Map<SingularAttribute<Ride, String>, String> strParams = collectLikeStringParams(filterParams);
            spec = cb.and(spec, handleLikeStringParams(strParams).toPredicate(root, query, cb));
            Map<SingularAttribute<Ride, ? extends Enum<?>>, String> enumParams = collectEqualEnumParams(filterParams);
            spec = cb.and(spec, handleEqualEnumParams(enumParams).toPredicate(root, query, cb));
            Map<SingularAttribute<Ride, UUID>, UUID> uuidParams = collectEqualUUIDParams(filterParams);
            spec = cb.and(spec, handleEqualUUIDParams(uuidParams).toPredicate(root, query, cb));
            Map<SingularAttribute<Ride, ZonedDateTime>, DateRange> dateRangeParams = collectBetweenDateRangeParams(
                    filterParams);
            spec = cb.and(spec, handleBetweenDateRangeParams(dateRangeParams).toPredicate(root, query, cb));

            return spec;
        };
    }

    private Map<SingularAttribute<Ride, String>, String> collectLikeStringParams(RidesFilterParams filterParams) {
        Map<SingularAttribute<Ride, String>, String> params = new HashMap<>();
        params.put(Ride_.startAddress, filterParams.startAddress());
        params.put(Ride_.destinationAddress, filterParams.destinationAddress());
        return params;
    }

    private Map<SingularAttribute<Ride, ? extends Enum<?>>, String> collectEqualEnumParams(
            RidesFilterParams filterParams) {
        Map<SingularAttribute<Ride, ? extends Enum<?>>, String> params = new HashMap<>();
        params.put(Ride_.status, filterParams.status());
        params.put(Ride_.paymentMethod, filterParams.paymentMethod());
        return params;
    }

    private Map<SingularAttribute<Ride, UUID>, UUID> collectEqualUUIDParams(RidesFilterParams filterParams) {
        Map<SingularAttribute<Ride, UUID>, UUID> params = new HashMap<>();
        params.put(Ride_.passengerId, filterParams.passengerId());
        params.put(Ride_.driverId, filterParams.driverId());
        return params;
    }

    private Map<SingularAttribute<Ride, ZonedDateTime>, DateRange> collectBetweenDateRangeParams(
            RidesFilterParams filterParams) {
        Map<SingularAttribute<Ride, ZonedDateTime>, DateRange> params = new HashMap<>();
        params.put(Ride_.bookRide, filterParams.bookRide());
        params.put(Ride_.cancelRide, filterParams.cancelRide());
        params.put(Ride_.acceptRide, filterParams.acceptRide());
        params.put(Ride_.beginRide, filterParams.beginRide());
        params.put(Ride_.endRide, filterParams.endRide());
        params.put(Ride_.finishRide, filterParams.finishRide());
        return params;
    }

    private Specification<Ride> handleLikeStringParams(Map<SingularAttribute<Ride, String>, String> strParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();

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

    private Specification<Ride> handleEqualEnumParams(
            Map<SingularAttribute<Ride, ? extends Enum<?>>, String> enumParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();

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

    private Specification<Ride> handleEqualUUIDParams(Map<SingularAttribute<Ride, UUID>, UUID> uuidParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();

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

    private Specification<Ride> handleBetweenDateRangeParams(
            Map<SingularAttribute<Ride, ZonedDateTime>, DateRange> dateRangeParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();

            for (Map.Entry<SingularAttribute<Ride, ZonedDateTime>, DateRange> param : dateRangeParams.entrySet()) {
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
