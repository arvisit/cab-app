package by.arvisit.cabapp.paymentservice.dto;

import static by.arvisit.cabapp.common.util.ValidationRegexp.CARD_NUMBER;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.UUID;

import by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record DriverPaymentRequestDto(
        @NotNull(message = "{by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto.driverId.NotNull.message}")
        @UUID
        String driverId,
        @NotNull(message = "{by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto.amount.NotNull.message}")
        @Positive(message = "{by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto.amount.Positive.message}")
        BigDecimal amount,
        @NotBlank(message = "{by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto.operation.NotBlank.message}")
        String operation,
        @NotBlank(message = "{by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto.cardNumber.NotBlank.message}")
        @Pattern(regexp = CARD_NUMBER,
                message = "{by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto.cardNumber.Pattern.message}")
        String cardNumber) {

    @AssertTrue(message = "{by.arvisit.cabapp.paymentservice.dto.DriverPaymentRequestDto.isValidOperation.message}")
    private boolean isValidOperation() {
        if (operation != null) {
            try {
                OperationTypeEnum.valueOf(operation);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return true;
    }
}
