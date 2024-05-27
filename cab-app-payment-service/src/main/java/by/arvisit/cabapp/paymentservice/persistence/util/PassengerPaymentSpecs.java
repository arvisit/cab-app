package by.arvisit.cabapp.paymentservice.persistence.util;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.util.DateRange;
import by.arvisit.cabapp.common.util.SpecUtil;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentsFilterParams;
import by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment;
import by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment_;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.SingularAttribute;

@Component
public class PassengerPaymentSpecs {

    public Specification<PassengerPayment> getAllByFilter(PassengerPaymentsFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            if (filterParams == null) {
                return spec;
            }

            Map<SingularAttribute<PassengerPayment, ? extends Enum<?>>, String> enumParams = collectEqualEnumParams(
                    filterParams);
            spec = cb.and(spec, handleEqualEnumParams(enumParams).toPredicate(root, query, cb));
            Map<SingularAttribute<PassengerPayment, UUID>, UUID> uuidParams = collectEqualUUIDParams(filterParams);
            spec = cb.and(spec, handleEqualUUIDParams(uuidParams).toPredicate(root, query, cb));
            Map<SingularAttribute<PassengerPayment, ZonedDateTime>, DateRange> dateRangeParams = collectBetweenDateRangeParams(
                    filterParams);
            spec = cb.and(spec, handleBetweenDateRangeParams(dateRangeParams).toPredicate(root, query, cb));

            return spec;
        };
    }

    private Map<SingularAttribute<PassengerPayment, ? extends Enum<?>>, String> collectEqualEnumParams(
            PassengerPaymentsFilterParams filterParams) {
        Map<SingularAttribute<PassengerPayment, ? extends Enum<?>>, String> params = new HashMap<>();
        params.put(PassengerPayment_.status, filterParams.status());
        params.put(PassengerPayment_.paymentMethod, filterParams.paymentMethod());
        return params;
    }

    private Map<SingularAttribute<PassengerPayment, UUID>, UUID> collectEqualUUIDParams(
            PassengerPaymentsFilterParams filterParams) {
        Map<SingularAttribute<PassengerPayment, UUID>, UUID> params = new HashMap<>();
        params.put(PassengerPayment_.driverId, filterParams.driverId());
        params.put(PassengerPayment_.passengerId, filterParams.passengerId());
        params.put(PassengerPayment_.rideId, filterParams.rideId());
        return params;
    }

    private Map<SingularAttribute<PassengerPayment, ZonedDateTime>, DateRange> collectBetweenDateRangeParams(
            PassengerPaymentsFilterParams filterParams) {
        Map<SingularAttribute<PassengerPayment, ZonedDateTime>, DateRange> params = new HashMap<>();
        params.put(PassengerPayment_.timestamp, filterParams.timestamp());
        return params;
    }

    private Specification<PassengerPayment> handleEqualEnumParams(
            Map<SingularAttribute<PassengerPayment, ? extends Enum<?>>, String> enumParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();

            for (Map.Entry<SingularAttribute<PassengerPayment, ? extends Enum<?>>, String> param : enumParams
                    .entrySet()) {
                SingularAttribute<PassengerPayment, ? extends Enum<?>> key = param.getKey();
                String value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.equalEnum(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }

    private Specification<PassengerPayment> handleEqualUUIDParams(
            Map<SingularAttribute<PassengerPayment, UUID>, UUID> uuidParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();

            for (Map.Entry<SingularAttribute<PassengerPayment, UUID>, UUID> param : uuidParams.entrySet()) {
                SingularAttribute<PassengerPayment, UUID> key = param.getKey();
                UUID value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.equalUUID(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }

    private Specification<PassengerPayment> handleBetweenDateRangeParams(
            Map<SingularAttribute<PassengerPayment, ZonedDateTime>, DateRange> dateRangeParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();

            for (Map.Entry<SingularAttribute<PassengerPayment, ZonedDateTime>, DateRange> param : dateRangeParams
                    .entrySet()) {
                SingularAttribute<PassengerPayment, ZonedDateTime> key = param.getKey();
                DateRange value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.betweenDateRange(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }
}
