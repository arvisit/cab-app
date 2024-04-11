package by.arvisit.cabapp.ridesservice.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

    List<PromoCode> findAllByIsActiveTrue(Pageable pageable);

    boolean existsByKeywordAndIsActiveTrue(String keyword);

    Optional<PromoCode> findByKeywordAndIsActiveTrue(String keyword);

    long countByIsActiveTrue();

}
