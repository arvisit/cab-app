package by.arvisit.cabapp.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Profile({ "dev" })
@FeignClient(name = "cab-app-driver-service", configuration = CabAppFeignClientConfiguration.class)
@CircuitBreaker(name = "discoveryDriverClient")
public interface DiscoveryDriverClient extends DriverClient {

    @Override
    @GetMapping("/api/v1/drivers/{id}")
    DriverResponseDto getDriverById(@PathVariable String id);

}
