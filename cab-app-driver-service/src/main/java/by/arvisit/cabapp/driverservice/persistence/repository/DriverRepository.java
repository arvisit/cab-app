package by.arvisit.cabapp.driverservice.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import by.arvisit.cabapp.driverservice.persistence.model.Driver;


public interface DriverRepository extends JpaRepository<Driver, UUID> {

    Optional<Driver> findByEmail(String email);

    boolean existsByEmail(String email);
}
