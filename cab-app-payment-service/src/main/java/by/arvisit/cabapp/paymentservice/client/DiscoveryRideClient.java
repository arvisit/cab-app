package by.arvisit.cabapp.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import by.arvisit.cabapp.common.dto.rides.RideResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Profile({ "dev" })
@FeignClient(name = "cab-app-rides-service",
        configuration = { CabAppFeignClientConfiguration.class, OAuthFeignConfig.class })
@CircuitBreaker(name = "discoveryRideClient")
public interface DiscoveryRideClient extends RideClient {

    @Override
    @GetMapping("/api/v1/rides/{id}")
    RideResponseDto getRideById(@PathVariable String id);

}
