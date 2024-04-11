package by.arvisit.cabapp.paymentservice.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment;
import java.util.List;

public interface PassengerPaymentRepository extends JpaRepository<PassengerPayment, UUID> {

    @Query("""
            SELECT COUNT(p) > 0
            FROM PassengerPayment p
            WHERE p.rideId = :rideId
                AND p.passengerId = :passengerId
                AND p.driverId = :driverId
                AND p.status = by.arvisit.cabapp.paymentservice.persistence.model.PaymentStatusEnum.SUCCESS
            """)
    boolean existsSuccessfulPayment(@Param("rideId") UUID rideId, @Param("passengerId") UUID passengerId,
            @Param("driverId") UUID driverId);

    List<PassengerPayment> findByRideId(UUID rideId);
}
