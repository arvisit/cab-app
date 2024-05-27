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

            Map<SingularAttribute<DriverPayment, ? extends Enum<?>>, String> enumParams = collectEqualEnumParams(
                    filterParams);
            spec = cb.and(spec, handleEqualEnumParams(enumParams).toPredicate(root, query, cb));
            Map<SingularAttribute<DriverPayment, UUID>, UUID> uuidParams = collectEqualUUIDParams(filterParams);
            spec = cb.and(spec, handleEqualUUIDParams(uuidParams).toPredicate(root, query, cb));
            Map<SingularAttribute<DriverPayment, ZonedDateTime>, DateRange> dateRangeParams = collectBetweenDateRangeParams(
                    filterParams);
            spec = cb.and(spec, handleBetweenDateRangeParams(dateRangeParams).toPredicate(root, query, cb));

            return spec;
        };
    }

    private Map<SingularAttribute<DriverPayment, ? extends Enum<?>>, String> collectEqualEnumParams(
            DriverPaymentsFilterParams filterParams) {
        Map<SingularAttribute<DriverPayment, ? extends Enum<?>>, String> params = new HashMap<>();
        params.put(DriverPayment_.status, filterParams.status());
        params.put(DriverPayment_.operation, filterParams.operation());
        return params;
    }

    private Map<SingularAttribute<DriverPayment, UUID>, UUID> collectEqualUUIDParams(
            DriverPaymentsFilterParams filterParams) {
        Map<SingularAttribute<DriverPayment, UUID>, UUID> params = new HashMap<>();
        params.put(DriverPayment_.driverId, filterParams.driverId());
        return params;
    }

    private Map<SingularAttribute<DriverPayment, ZonedDateTime>, DateRange> collectBetweenDateRangeParams(
            DriverPaymentsFilterParams filterParams) {
        Map<SingularAttribute<DriverPayment, ZonedDateTime>, DateRange> params = new HashMap<>();
        params.put(DriverPayment_.timestamp, filterParams.timestamp());
        return params;
    }

    private Specification<DriverPayment> handleEqualEnumParams(
            Map<SingularAttribute<DriverPayment, ? extends Enum<?>>, String> enumParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();

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

    private Specification<DriverPayment> handleEqualUUIDParams(
            Map<SingularAttribute<DriverPayment, UUID>, UUID> uuidParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();

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

    private Specification<DriverPayment> handleBetweenDateRangeParams(
            Map<SingularAttribute<DriverPayment, ZonedDateTime>, DateRange> dateRangeParams) {
        return (root, query, cb) -> {
            Predicate spec = cb.conjunction();

            for (Map.Entry<SingularAttribute<DriverPayment, ZonedDateTime>, DateRange> param : dateRangeParams
                    .entrySet()) {
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
