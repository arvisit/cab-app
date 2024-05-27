package by.arvisit.cabapp.driverservice.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import by.arvisit.cabapp.driverservice.persistence.model.Driver;

public interface DriverRepository extends JpaRepository<Driver, UUID>, JpaSpecificationExecutor<Driver> {

    Optional<Driver> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Driver> findByIsAvailableTrue(Pageable pageable);

    long countByIsAvailableTrue();
}
