package by.arvisit.cabapp.ridesservice.service;

import java.math.BigDecimal;

import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;

public interface CostService {

    BigDecimal calculateInitialRideCost(String startAddress, String destinationAddress);

    BigDecimal calculateRideCostWithPromoCode(BigDecimal initialCost, PromoCode promoCode);
}
