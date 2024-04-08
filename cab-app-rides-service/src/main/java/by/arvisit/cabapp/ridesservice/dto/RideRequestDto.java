package by.arvisit.cabapp.ridesservice.dto;

import org.hibernate.validator.constraints.UUID;

import by.arvisit.cabapp.ridesservice.persistence.model.PaymentMethodEnum;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record RideRequestDto(
        @UUID
        String passengerId,
        @NotBlank(message = "{by.arvisit.cabapp.ridesservice.dto.RideRequestDto.startAddress.NotBlank.message}")
        @Size(max = 100, message = "{by.arvisit.cabapp.ridesservice.dto.RideRequestDto.startAddress.Size.message}")
        String startAddress,
        @NotBlank(message = "{by.arvisit.cabapp.ridesservice.dto.RideRequestDto.destinationAddress.NotBlank.message}")
        @Size(max = 100, message = "{by.arvisit.cabapp.ridesservice.dto.RideRequestDto.destinationAddress.Size.message}")
        String destinationAddress,
        @NotBlank(message = "{by.arvisit.cabapp.ridesservice.dto.RideRequestDto.paymentMethod.NotBlank.message}")
        String paymentMethod) {

    @AssertTrue(message = "{by.arvisit.cabapp.ridesservice.dto.RideRequestDto.paymentMethod.isValidPaymentMethod.message}")
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
