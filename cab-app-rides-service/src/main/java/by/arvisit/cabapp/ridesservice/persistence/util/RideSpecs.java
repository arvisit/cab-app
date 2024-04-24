package by.arvisit.cabapp.ridesservice.persistence.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import by.arvisit.cabapp.ridesservice.persistence.model.Ride;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Component
public class RideSpecs {

    private static final String DATE_AS_FILTER_PARAM_REGEXP = "[0-9]{4}(-(0[1-9]|1[0-2]))?(-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1]))?(-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])T(0[1-9]|1[0-9]|2[0-3]))?";
    private static final Set<String> VALID_STRING_PARAM_NAMES = Set.of("passengerId", "driverId", "status",
            "paymentMethod", "startAddress", "destinationAddress");
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
                if (VALID_STRING_PARAM_NAMES.contains(paramKey) && !paramValue.trim().isEmpty()) {
                    String likePattern = toLikePattern(paramValue);
                    spec = cb.and(spec, cb.like(cb.lower(root.get(paramKey)), likePattern));
                }
                if (VALID_DATE_PARAM_NAMES.contains(paramKey) && isParseableDate(paramValue)) {
                    DateRange dateRange = parseToDateRange(paramValue);
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

    private boolean isParseableDate(String str) {
        Pattern pattern = Pattern.compile(DATE_AS_FILTER_PARAM_REGEXP);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    private DateRange parseToDateRange(String strDate) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (strDate.length() == 13) {
            startDate = LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"));
            endDate = startDate.plusHours(1).minusNanos(1);
        }
        if (strDate.length() == 10) {
            startDate = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            endDate = startDate.plusDays(1).minusNanos(1);
        }
        if (strDate.length() == 7) {
            startDate = YearMonth.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM")).atDay(1).atStartOfDay();
            endDate = startDate.plusMonths(1).minusNanos(1);
        }
        if (strDate.length() == 4) {
            startDate = Year.parse(strDate).atMonth(Month.JANUARY).atDay(1).atStartOfDay();
            endDate = startDate.plusYears(1).minusNanos(1);
        }
        return DateRange.builder()
                .withStartDate(startDate)
                .withEndDate(endDate)
                .build();
    }

    @Getter
    @Setter
    @Builder(setterPrefix = "with")
    private static class DateRange {
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }
}
