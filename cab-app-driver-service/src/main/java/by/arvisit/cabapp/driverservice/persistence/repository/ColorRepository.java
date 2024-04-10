package by.arvisit.cabapp.driverservice.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import by.arvisit.cabapp.driverservice.persistence.model.Color;

public interface ColorRepository extends JpaRepository<Color, Integer> {

}
