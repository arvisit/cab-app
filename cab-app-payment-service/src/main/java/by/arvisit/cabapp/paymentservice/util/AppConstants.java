package by.arvisit.cabapp.paymentservice.util;

import java.math.BigDecimal;
import java.time.ZoneId;

public final class AppConstants {

    public static final BigDecimal AGGREGATOR_FEE_PERCENT = BigDecimal.valueOf(5);
    public static final ZoneId EUROPE_MINSK_TIMEZONE = ZoneId.of("Europe/Minsk");
    public static final BigDecimal BIG_DECIMAL_HUNDRED = BigDecimal.valueOf(100);

    private AppConstants() {
    }

}
