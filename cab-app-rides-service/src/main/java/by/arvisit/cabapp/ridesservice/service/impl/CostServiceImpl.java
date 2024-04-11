package by.arvisit.cabapp.ridesservice.service.impl;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.stereotype.Service;

import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;
import by.arvisit.cabapp.ridesservice.service.CostService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CostServiceImpl implements CostService {

    @Override
    public BigDecimal calculateInitialRideCost(String startAddress, String destinationAddress) {
        log.debug("Call for CostService.calculateInitialRideCost() with start address: {}, and destination address: {}",
                startAddress, destinationAddress);

        Random random = new Random();

        int dollarsMinor = (random.nextInt(0, 50) + 1) * 100;
        int centsMinor = random.nextInt(0, 100);
        int costMinor = dollarsMinor + centsMinor;

        return BigDecimal.valueOf(costMinor / 100.0);
    }

    @Override
    public BigDecimal calculateRideCostWithPromoCode(BigDecimal initialCost, PromoCode promoCode) {
        log.debug(
                "Call for CostService.calculateRideCostWithPromoCode() with initial cost: {}, and promo code keyword: '{}' and discount percent: {}",
                initialCost, promoCode.getKeyword(), promoCode.getDiscountPercent());

        BigDecimal discountAbsolute = BigDecimal.valueOf(promoCode.getDiscountPercent() / 100.0);
        BigDecimal decrement = initialCost.multiply(discountAbsolute);

        return initialCost.subtract(decrement);
    }

}
