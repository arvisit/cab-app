package by.arvisit.cabapp.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfig {

    @Bean
    RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("cab-app-passenger-service", r -> r.path(
                        "/api/v1/passengers/**")
                        .uri("lb://cab-app-passenger-service"))
                .route("cab-app-driver-service", r -> r.path(
                        "/api/v1/drivers/**",
                        "/api/v1/cars/**",
                        "/api/v1/car-manufacturers/**",
                        "/api/v1/colors/**")
                        .uri("lb://cab-app-driver-service"))
                .route("cab-app-rides-service", r -> r.path(
                        "/api/v1/rides/**",
                        "/api/v1/promo-codes/**")
                        .uri("lb://cab-app-rides-service"))
                .route("cab-app-payment-service", r -> r.path(
                        "/api/v1/passenger-payments/**",
                        "/api/v1/driver-payments/**")
                        .uri("lb://cab-app-payment-service"))
                .build();
    }
}
