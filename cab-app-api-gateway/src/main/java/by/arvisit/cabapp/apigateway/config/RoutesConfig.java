package by.arvisit.cabapp.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RoutesConfig {

    private final CircuitBreakerNamesRegistry circuitBreakerNamesRegistry;

    @Bean
    RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("cab-app-passenger-service", r -> r.path(
                        "/api/v1/passengers/**")
                        .filters(f -> f.circuitBreaker(
                                c -> c.setName(circuitBreakerNamesRegistry.getPassengerCircuitBreaker())))
                        .uri("lb://cab-app-passenger-service"))
                .route("cab-app-driver-service", r -> r.path(
                        "/api/v1/drivers/**",
                        "/api/v1/cars/**",
                        "/api/v1/car-manufacturers/**",
                        "/api/v1/colors/**")
                        .filters(f -> f.circuitBreaker(
                                c -> c.setName(circuitBreakerNamesRegistry.getDriverCircuitBreaker())))
                        .uri("lb://cab-app-driver-service"))
                .route("cab-app-rides-service", r -> r.path(
                        "/api/v1/rides/**",
                        "/api/v1/promo-codes/**")
                        .filters(f -> f.circuitBreaker(
                                c -> c.setName(circuitBreakerNamesRegistry.getRidesCircuitBreaker())))
                        .uri("lb://cab-app-rides-service"))
                .route("cab-app-payment-service", r -> r.path(
                        "/api/v1/passenger-payments/**",
                        "/api/v1/driver-payments/**")
                        .filters(f -> f.circuitBreaker(
                                c -> c.setName(circuitBreakerNamesRegistry.getPaymentCircuitBreaker())))
                        .uri("lb://cab-app-payment-service"))
                .build();
    }
}
