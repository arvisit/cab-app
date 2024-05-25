package by.arvisit.cabapp.paymentservice.persistence.util;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.util.DateRange;
import by.arvisit.cabapp.common.util.SpecUtil;
import by.arvisit.cabapp.paymentservice.dto.DriverPaymentsFilterParams;
import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment;
import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment_;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.metamodel.SingularAttribute;

@Component
public class DriverPaymentSpecs {

    public Specification<DriverPayment> getAllByFilter(DriverPaymentsFilterParams filterParams) {
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

    private Specification<DriverPayment> handleEqualEnumParams(DriverPaymentsFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<DriverPayment, ? extends Enum<?>>, String> enumParams = new HashMap<>();
            enumParams.put(DriverPayment_.status, filterParams.status());
            enumParams.put(DriverPayment_.operation, filterParams.operation());

            for (Map.Entry<SingularAttribute<DriverPayment, ? extends Enum<?>>, String> param : enumParams.entrySet()) {
                SingularAttribute<DriverPayment, ? extends Enum<?>> key = param.getKey();
                String value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.equalEnum(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }

    private Specification<DriverPayment> handleEqualUUIDParams(DriverPaymentsFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<DriverPayment, UUID>, UUID> uuidParams = new HashMap<>();
            uuidParams.put(DriverPayment_.driverId, filterParams.driverId());

            for (Map.Entry<SingularAttribute<DriverPayment, UUID>, UUID> param : uuidParams.entrySet()) {
                SingularAttribute<DriverPayment, UUID> key = param.getKey();
                UUID value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.equalUUID(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }

    private Specification<DriverPayment> handleBetweenDateRangeParams(DriverPaymentsFilterParams filterParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();
            Map<SingularAttribute<DriverPayment, ZonedDateTime>, DateRange> dateParams = new HashMap<>();
            dateParams.put(DriverPayment_.timestamp, filterParams.timestamp());

            for (Map.Entry<SingularAttribute<DriverPayment, ZonedDateTime>, DateRange> param : dateParams.entrySet()) {
                SingularAttribute<DriverPayment, ZonedDateTime> key = param.getKey();
                DateRange value = param.getValue();
                spec = (value != null)
                        ? cb.and(spec, SpecUtil.betweenDateRange(value, key).toPredicate(root, query, cb))
                        : spec;
            }
            return spec;
        };
    }
}
