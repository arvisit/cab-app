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

            spec = cb.and(spec, handleEqualEnumParams(filterParams).toPredicate(root, query, cb));
            spec = cb.and(spec, handleEqualUUIDParams(filterParams).toPredicate(root, query, cb));
            spec = cb.and(spec, handleBetweenDateRangeParams(filterParams).toPredicate(root, query, cb));

            return spec;
        };
    }

    private Specification<PassengerPayment> handleEqualEnumParams(PassengerPaymentsFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<PassengerPayment, ? extends Enum<?>>, String> enumParams = new HashMap<>();
            enumParams.put(PassengerPayment_.status, filterParams.status());
            enumParams.put(PassengerPayment_.paymentMethod, filterParams.paymentMethod());

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

    private Specification<PassengerPayment> handleEqualUUIDParams(PassengerPaymentsFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<PassengerPayment, UUID>, UUID> uuidParams = new HashMap<>();
            uuidParams.put(PassengerPayment_.driverId, filterParams.driverId());
            uuidParams.put(PassengerPayment_.passengerId, filterParams.passengerId());
            uuidParams.put(PassengerPayment_.rideId, filterParams.rideId());

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

    private Specification<PassengerPayment> handleBetweenDateRangeParams(PassengerPaymentsFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<PassengerPayment, ZonedDateTime>, DateRange> dateParams = new HashMap<>();
            dateParams.put(PassengerPayment_.timestamp, filterParams.timestamp());

            for (Map.Entry<SingularAttribute<PassengerPayment, ZonedDateTime>, DateRange> param : dateParams
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
