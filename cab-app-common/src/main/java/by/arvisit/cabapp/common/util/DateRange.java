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

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static DateRange fromSingleValue(String strDate) {
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
}
