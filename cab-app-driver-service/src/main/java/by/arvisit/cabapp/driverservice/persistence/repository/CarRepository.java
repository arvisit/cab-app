package by.arvisit.cabapp.driverservice.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import by.arvisit.cabapp.driverservice.persistence.model.Car;

public interface CarRepository extends JpaRepository<Car, UUID> {

}
