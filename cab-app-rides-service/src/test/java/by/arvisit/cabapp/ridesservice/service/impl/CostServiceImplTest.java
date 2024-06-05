package by.arvisit.cabapp.ridesservice.service.impl;

import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_DESTINATION_ADDRESS;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_INITIAL_COST;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_START_ADDRESS;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPromoCode;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import by.arvisit.cabapp.ridesservice.service.CostService;

class CostServiceImplTest {

    private static final BigDecimal MIN_COST = BigDecimal.valueOf(1.00);
    private static final BigDecimal MAX_COST = BigDecimal.valueOf(50.99);

    @Autowired
    private CostService costService = new CostServiceImpl();

    @Test
    void shouldReturnCorrectCost_whenCalculateInitialRideCost() {
        BigDecimal cost = costService.calculateInitialRideCost(RIDE_DEFAULT_START_ADDRESS,
                RIDE_DEFAULT_DESTINATION_ADDRESS);

        assertThat(cost)
                .isBetween(MIN_COST, MAX_COST);
    }

    @Test
    void shouldReturnCorrectCost_whenCalculateRideCostWithPromoCode() {
        BigDecimal cost = costService.calculateRideCostWithPromoCode(RIDE_DEFAULT_INITIAL_COST, getPromoCode().build());

        assertThat(cost)
                .isEqualByComparingTo(RIDE_DEFAULT_FINAL_COST_WITH_PROMO_CODE);
    }
}
