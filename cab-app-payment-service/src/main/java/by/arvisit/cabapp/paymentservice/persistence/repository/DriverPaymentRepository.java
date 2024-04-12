package by.arvisit.cabapp.paymentservice.persistence.repository;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.arvisit.cabapp.paymentservice.persistence.model.DriverPayment;

public interface DriverPaymentRepository extends JpaRepository<DriverPayment, UUID> {

    @Query("""
            SELECT
                SUM(
                    CASE 
                        WHEN p.paymentMethod = by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum.BANK_CARD
                        THEN COALESCE(p.amount - p.feeAmount, 0)
                        ELSE 0 
                    END
                ) -
                SUM(
                    CASE 
                        WHEN p.paymentMethod = by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum.CASH
                        THEN COALESCE(p.feeAmount, 0)
                        ELSE 0 
                    END
                ) -
                SUM(
                    CASE 
                        WHEN d.operation = by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum.WITHDRAWAL
                        THEN COALESCE(d.amount, 0)
                        ELSE 0 
                    END
                ) +
                SUM(
                    CASE 
                        WHEN d.operation = by.arvisit.cabapp.paymentservice.persistence.model.OperationTypeEnum.REPAYMENT
                        THEN COALESCE(d.amount, 0)
                        ELSE 0 
                    END
                )
            FROM PassengerPayment p
                LEFT JOIN DriverPayment d ON p.driverId = d.driverId
            WHERE p.driverId = :id
                AND p.status = by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum.SUCCESS
                AND (d.status = by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum.SUCCESS
                    OR d.status IS NULL)
            """)
    BigDecimal getDriverAccountBalance(@Param("id") UUID id);
}
