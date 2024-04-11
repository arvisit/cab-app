package by.arvisit.cabapp.paymentservice.persistence.repository;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment;

public interface DriverPaymentRepository extends JpaRepository<DriverPayment, UUID> {

    @Query("""
            SELECT (
                (SELECT COALESCE(SUM(p.amount - p.feeAmount), 0)
                FROM PassengerPayment p
                WHERE p.driverId = :id
                    AND p.paymentMethod = by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum.BANK_CARD
                    AND p.status = by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum.SUCCESS) -
                (SELECT COALESCE(SUM(p.feeAmount), 0)
                FROM PassengerPayment p
                WHERE p.driverId = :id
                    AND p.paymentMethod = by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum.CASH
                    AND p.status = by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum.SUCCESS) -
                (SELECT COALESCE(SUM(d.amount), 0)
                FROM DriverPayment d
                WHERE d.driverId = :id
                    AND d.operation = by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum.WITHDRAWAL
                    AND d.status = by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum.SUCCESS) +
                (SELECT COALESCE(SUM(d.amount), 0)
                FROM DriverPayment d
                WHERE d.driverId = :id
                    AND d.operation = by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum.REPAYMENT
                    AND d.status = by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum.SUCCESS))
            """)
    BigDecimal getDriverAccountBalance(@Param("id") UUID id);
}
