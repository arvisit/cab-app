package by.arvisit.cabapp.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Builder(setterPrefix = "with")
public class DateRange {

    private static final int YEAR_MONTH_DAY_HOUR_STR_LENGTH = 13;
    private static final int YEAR_MONTH_DAY_STR_LENGTH = 10;
    private static final int YEAR_MONTH_STR_LENGTH = 7;
    private static final int YEAR_STR_LENGTH = 4;

    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    public static DateRange fromSingleValue(String strDate) {
        ZonedDateTime startDate = null;
        ZonedDateTime endDate = null;
        if (strDate.length() == YEAR_MONTH_DAY_HOUR_STR_LENGTH) {
            startDate = LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"))
                    .atZone(CommonConstants.EUROPE_MINSK_TIMEZONE);
            endDate = startDate.plusHours(1).minusNanos(1);
        }
        if (strDate.length() == YEAR_MONTH_DAY_STR_LENGTH) {
            startDate = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .atStartOfDay()
                    .atZone(CommonConstants.EUROPE_MINSK_TIMEZONE);
            endDate = startDate.plusDays(1).minusNanos(1);
        }
        if (strDate.length() == YEAR_MONTH_STR_LENGTH) {
            startDate = YearMonth.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM"))
                    .atDay(1)
                    .atStartOfDay()
                    .atZone(CommonConstants.EUROPE_MINSK_TIMEZONE);
            endDate = startDate.plusMonths(1).minusNanos(1);
        }
        if (strDate.length() == YEAR_STR_LENGTH) {
            startDate = Year.parse(strDate).atMonth(Month.JANUARY)
                    .atDay(1)
                    .atStartOfDay()
                    .atZone(CommonConstants.EUROPE_MINSK_TIMEZONE);
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
