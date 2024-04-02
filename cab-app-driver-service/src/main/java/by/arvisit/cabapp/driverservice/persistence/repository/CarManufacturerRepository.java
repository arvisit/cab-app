package by.arvisit.cabapp.driverservice.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import by.arvisit.cabapp.driverservice.persistence.model.CarManufacturer;

public interface CarManufacturerRepository extends JpaRepository<CarManufacturer, Integer> {

}
