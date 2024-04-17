package by.arvisit.cabapp.ridesservice.util;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentVerifier {
    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String PAYMENT_NOT_SUCCESS_STATUS_MESSAGE_TEMPLATE = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.isPaid.IllegalStateException.template";
    private final MessageSource messageSource;

    public void verifyPaymentStatus(PassengerPaymentResponseDto payment) {
        if (!SUCCESS_STATUS.equals(payment.status())) {

            String errorMessage = messageSource.getMessage(
                    PAYMENT_NOT_SUCCESS_STATUS_MESSAGE_TEMPLATE,
                    new Object[] {}, null);
            throw new IllegalStateException(errorMessage);
        }
    }

}
