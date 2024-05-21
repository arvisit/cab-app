package by.arvisit.cabapp.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import by.arvisit.cabapp.common.dto.rides.RideResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Profile({ "itest", "contract" })
@FeignClient(name = "cab-app-rides-service", url = "${spring.settings.cab-app-rides-service.uri}",
        configuration = CabAppFeignClientConfiguration.class)
@CircuitBreaker(name = "directRideClient")
public interface DirectRideClient extends RideClient {

    @Override
    @GetMapping("/{id}")
    RideResponseDto getRideById(@PathVariable String id);

}
