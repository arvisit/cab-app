package by.arvisit.cabapp.ridesservice.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import by.arvisit.cabapp.ridesservice.persistence.model.Ride;

public interface RideRepository extends JpaRepository<Ride, UUID>, JpaSpecificationExecutor<Ride> {

    List<Ride> findByPassengerId(UUID passengerId, Pageable pageable);

    List<Ride> findByDriverId(UUID driverId, Pageable pageable);

    @Query("SELECT AVG(r.passengerScore) FROM Ride r WHERE r.passengerId = :id")
    Optional<Double> getPassengerAverageScore(UUID id);

    @Query("SELECT AVG(r.driverScore) FROM Ride r WHERE r.driverId = :id")
    Optional<Double> getDriverAverageScore(UUID id);

    long countByPassengerId(UUID passengerId);

    long countByDriverId(UUID driverId);
}
