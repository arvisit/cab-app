package by.arvisit.cabapp.paymentservice.service.impl;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import by.arvisit.cabapp.paymentservice.client.DriverClient;
import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment;
import by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum;
import by.arvisit.cabapp.paymentservice.persistence.repository.DriverPaymentRepository;
import by.arvisit.cabapp.paymentservice.util.PaymentTestData;

@ExtendWith(MockitoExtension.class)
class DriverPaymentVerifierTest {

    @InjectMocks
    private DriverPaymentVerifier driverPaymentVerifier;
    @Mock
    private DriverPaymentRepository driverPaymentRepository;
    @Mock
    private MessageSource messageSource;
    @Mock
    private DriverClient driverClient;

    @Test
    void shouldThrowNoException_whenVerifyNewPaymentWithValidPayment() {
        DriverPaymentVerifier verifierSpy = Mockito.spy(driverPaymentVerifier);

        doNothing().when(verifierSpy)
                .verifyDriverForNewPayment(Mockito.any(DriverPayment.class));
        doNothing().when(verifierSpy)
                .verifyBalanceForNewPayment(Mockito.any(DriverPayment.class));

        DriverPayment payment = PaymentTestData.getDriverPayment().build();

        assertThatNoException().isThrownBy(() -> verifierSpy.verifyNewPayment(payment));
    }

    @Test
    void shouldThrowNoException_whenVerifyBalanceForNewPaymentWithRepayment() {
        BigDecimal balance = PaymentTestData.DRIVER_ACCOUNT_BALANCE_ENOUGH;

        when(driverPaymentRepository.getDriverAccountBalance(Mockito.any(UUID.class)))
                .thenReturn(balance);

        DriverPayment payment = PaymentTestData.getDriverPayment()
                .withOperation(OperationTypeEnum.REPAYMENT)
                .build();

        assertThatNoException().isThrownBy(() -> driverPaymentVerifier.verifyBalanceForNewPayment(payment));
    }

    @Test
    void shouldThrowNoException_whenVerifyBalanceForNewPaymentWithPositiveBalanceAndFitAmount() {
        BigDecimal balance = PaymentTestData.DRIVER_ACCOUNT_BALANCE_ENOUGH;

        when(driverPaymentRepository.getDriverAccountBalance(Mockito.any(UUID.class)))
                .thenReturn(balance);

        DriverPayment payment = PaymentTestData.getDriverPayment()
                .withOperation(OperationTypeEnum.WITHDRAWAL)
                .build();

        assertThatNoException().isThrownBy(() -> driverPaymentVerifier.verifyBalanceForNewPayment(payment));
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyBalanceForNewPaymentWithNegativeBalance() {
        BigDecimal balance = PaymentTestData.DRIVER_ACCOUNT_BALANCE_NEGATIVE;

        when(driverPaymentRepository.getDriverAccountBalance(Mockito.any(UUID.class)))
                .thenReturn(balance);

        DriverPayment payment = PaymentTestData.getDriverPayment()
                .withOperation(OperationTypeEnum.WITHDRAWAL)
                .build();

        assertThatThrownBy(() -> driverPaymentVerifier.verifyBalanceForNewPayment(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyBalanceForNewPaymentWithPositiveBalanceAndNotFitAmount() {
        BigDecimal balance = PaymentTestData.DRIVER_ACCOUNT_BALANCE_NOT_ENOUGH;

        when(driverPaymentRepository.getDriverAccountBalance(Mockito.any(UUID.class)))
                .thenReturn(balance);

        DriverPayment payment = PaymentTestData.getDriverPayment()
                .withOperation(OperationTypeEnum.WITHDRAWAL)
                .build();

        assertThatThrownBy(() -> driverPaymentVerifier.verifyBalanceForNewPayment(payment))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowNoException_whenVerifyDriverForNewPaymentWithValidDriver() {
        DriverResponseDto driver = PaymentTestData.getDriverResponseDto().build();

        when(driverClient.getDriverById(Mockito.anyString()))
                .thenReturn(driver);

        DriverPayment payment = PaymentTestData.getDriverPayment().build();

        assertThatNoException().isThrownBy(() -> driverPaymentVerifier.verifyDriverForNewPayment(payment));
    }

    @Test
    void shouldThrowIllegalStateException_whenVerifyDriverForNewPaymentWithNoMatchForCardNumber() {
        DriverResponseDto driver = PaymentTestData.getDriverResponseDto().build();

        when(driverClient.getDriverById(Mockito.anyString()))
                .thenReturn(driver);

        DriverPayment payment = PaymentTestData.getDriverPayment()
                .withCardNumber(PaymentTestData.PASSENGER_CARD_NUMBER)
                .build();

        assertThatThrownBy(() -> driverPaymentVerifier.verifyDriverForNewPayment(payment))
                .isInstanceOf(IllegalStateException.class);
    }
}
