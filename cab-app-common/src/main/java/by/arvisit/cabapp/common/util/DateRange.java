package by.arvisit.cabapp.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
public class DateRange {

    private static final int YEAR_MONTH_DAY_HOUR_STR_LENGTH = 13;
    private static final int YEAR_MONTH_DAY_STR_LENGTH = 10;
    private static final int YEAR_MONTH_STR_LENGTH = 7;
    private static final int YEAR_STR_LENGTH = 4;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static DateRange fromSingleValue(String strDate) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (strDate.length() == YEAR_MONTH_DAY_HOUR_STR_LENGTH) {
            startDate = LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"));
            endDate = startDate.plusHours(1).minusNanos(1);
        }
        if (strDate.length() == YEAR_MONTH_DAY_STR_LENGTH) {
            startDate = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            endDate = startDate.plusDays(1).minusNanos(1);
        }
        if (strDate.length() == YEAR_MONTH_STR_LENGTH) {
            startDate = YearMonth.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM")).atDay(1).atStartOfDay();
            endDate = startDate.plusMonths(1).minusNanos(1);
        }
        if (strDate.length() == YEAR_STR_LENGTH) {
            startDate = Year.parse(strDate).atMonth(Month.JANUARY).atDay(1).atStartOfDay();
            endDate = startDate.plusYears(1).minusNanos(1);
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Provided value could not be parsed to date range");
        }
        return DateRange.builder()
                .withStartDate(startDate)
                .withEndDate(endDate)
                .build();
    }
}
