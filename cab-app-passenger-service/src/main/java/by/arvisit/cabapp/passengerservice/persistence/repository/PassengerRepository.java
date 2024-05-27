package by.arvisit.cabapp.passengerservice.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, UUID>, JpaSpecificationExecutor<Passenger> {

    Optional<Passenger> findByEmail(String email);

    boolean existsByEmail(String email);
}
