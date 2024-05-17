package by.arvisit.cabapp.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;

@Profile({ "dev" })
@FeignClient(value = "cab-app-passenger-service",        configuration = CabAppFeignClientConfiguration.class)
public interface DiscoveryPassengerClient extends PassengerClient {

    @Override
    @GetMapping("/api/v1/passengers/{id}")
    PassengerResponseDto getPassengerById(@PathVariable String id);
}
