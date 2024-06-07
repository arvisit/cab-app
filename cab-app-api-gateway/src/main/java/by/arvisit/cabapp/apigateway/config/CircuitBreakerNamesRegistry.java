package by.arvisit.cabapp.apigateway.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class CircuitBreakerNamesRegistry {

    private String passengerCircuitBreaker;
    private String driverCircuitBreaker;
    private String ridesCircuitBreaker;
    private String paymentCircuitBreaker;
    private List<String> all;

    public CircuitBreakerNamesRegistry() {
        all = new ArrayList<>();

        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getType().equals(List.class)) {
                continue;
            }

            field.setAccessible(true);
            try {
                field.set(this, field.getName());
                all.add(field.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
