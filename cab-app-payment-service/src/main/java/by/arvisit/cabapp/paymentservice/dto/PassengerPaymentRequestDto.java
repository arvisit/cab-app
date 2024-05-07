package by.arvisit.cabapp.paymentservice.dto;

import static by.arvisit.cabapp.common.util.ValidationRegexp.CARD_NUMBER;
import static by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum.BANK_CARD;
import static by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum.CASH;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.UUID;

import by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record PassengerPaymentRequestDto(
        @NotNull(message = "{by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto.rideId.NotNull.message}")
        @UUID
        String rideId,
        @NotNull(message = "{by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto.passengerId.NotNull.message}")
        @UUID
        String passengerId,
        @NotNull(message = "{by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto.driverId.NotNull.message}")
        @UUID
        String driverId,
        @NotNull(message = "{by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto.amount.NotNull.message}")
        @Positive(message = "{by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto.amount.Positive.message}")
        BigDecimal amount,
        @NotBlank(message = "{by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto.paymentMethod.NotBlank.message}")
        String paymentMethod,
        @Nullable
        @Pattern(regexp = CARD_NUMBER,
                message = "{by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto.cardNumber.Pattern.message}")
        String cardNumber) {

    @AssertTrue(message = "{by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto.isValidPaymentMethodAndCardNumberCombination.message}")
    private boolean isValidPaymentMethodAndCardNumberCombination() {
        if (paymentMethod == null) {
            return true;
        }
        if (!isValidPaymentMethod()) {
            return false;
        }
        PaymentMethodEnum paymentMethodValue = PaymentMethodEnum.valueOf(paymentMethod);
        return ((paymentMethodValue == BANK_CARD && cardNumber != null)
                || (paymentMethodValue == CASH && cardNumber == null));
    }

    @AssertTrue(message = "{by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto.isValidPaymentMethod.message}")
    private boolean isValidPaymentMethod() {
        if (paymentMethod != null) {
            try {
                PaymentMethodEnum.valueOf(paymentMethod);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }
}
