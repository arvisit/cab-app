package by.arvisit.cabapp.paymentservice.persistence.util;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import by.arvisit.cabapp.paymentservice.persistence.model.PassengerPayment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

@Component
public class PassengerPaymentSpecs {

    public Specification<PassengerPayment> getAllByFilter(Map<String, String> filterParams) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder cb = criteriaBuilder;
            Predicate spec = cb.conjunction();
            
            return spec;
        };
    }
}
