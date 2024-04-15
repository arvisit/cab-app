package by.arvisit.cabapp.paymentservice.service.impl;

import java.math.BigDecimal;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.paymentservice.client.DriverClient;
import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment;
import by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum;
import by.arvisit.cabapp.paymentservice.persistence.repository.DriverPaymentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class DriverPaymentVerifier {

    private static final String LOW_BALANCE_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment.balance.IllegalStateException.template";
    private static final String CARD_NUMBER_MISMATCH_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment.cardNumber.IllegalStateException.template";

    private final DriverPaymentRepository driverPaymentRepository;
    private final MessageSource messageSource;
    private final DriverClient driverClient;

    public void verifyNewPayment(DriverPayment newPayment) {
        verifyDriverForNewPayment(newPayment);
        verifyBalanceForNewPayment(newPayment);
    }

    public void verifyBalanceForNewPayment(DriverPayment newPayment) {
        BigDecimal balance = driverPaymentRepository.getDriverAccountBalance(
                newPayment.getDriverId());

        if (newPayment.getOperation() == OperationTypeEnum.WITHDRAWAL
                && (balance.compareTo(BigDecimal.ZERO) <= 0 || balance.compareTo(newPayment.getAmount()) < 0)) {

            String errorMessage = messageSource.getMessage(
                    LOW_BALANCE_MESSAGE_TEMPLATE_KEY,
                    new Object[] {}, null);
            throw new IllegalStateException(errorMessage);
        }
    }

    public void verifyDriverForNewPayment(DriverPayment newPayment) {
        DriverResponseDto driver = driverClient.getDriverById(newPayment.getDriverId().toString());

        if (!newPayment.getCardNumber().equals(driver.cardNumber())) {

            String errorMessage = messageSource.getMessage(
                    CARD_NUMBER_MISMATCH_MESSAGE_TEMPLATE_KEY,
                    new Object[] {}, null);
            throw new IllegalStateException(errorMessage);
        }
    }
}
