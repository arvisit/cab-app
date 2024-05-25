package by.arvisit.cabapp.common.util;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.metamodel.SingularAttribute;

public final class SpecUtil {

    private SpecUtil() {
    }

    public static String toLikePattern(String str) {
        return "%" + str.toLowerCase() + "%";
    }

    public static <T> Specification<T> likeString(String str, SingularAttribute<T, String> attribute) {
        return (root, query, cb) -> {
            String likePattern = SpecUtil.toLikePattern(str);
            return cb.like(cb.lower(root.get(attribute)), likePattern);
        };
    }

    public static <T> Specification<T> equalBoolean(Boolean bool, SingularAttribute<T, Boolean> attribute) {
        return (root, query, cb) -> cb.equal(root.get(attribute), bool);
    }

    public static <T> Specification<T> equalEnum(String str, SingularAttribute<T, ? extends Enum<?>> attribute) {
        return (root, query, cb) -> cb.equal(root.get(attribute), str);
    }

    public static <T> Specification<T> equalUUID(UUID uuid, SingularAttribute<T, UUID> attribute) {
        return (root, query, cb) -> cb.equal(root.get(attribute), uuid);
    }

    public static <T> Specification<T> betweenDateRange(DateRange dateRange,
            SingularAttribute<T, ZonedDateTime> attribute) {
        return (root, query, cb) -> cb.between(root.get(attribute), dateRange.getStartDate(),
                dateRange.getEndDate());
    }
}
