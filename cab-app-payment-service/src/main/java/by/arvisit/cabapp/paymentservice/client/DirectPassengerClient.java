package by.arvisit.cabapp.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;

@Profile({ "dev", "itest", "contract" })
@FeignClient(value = "cab-app-passenger-service", url = "${spring.settings.cab-app-passenger-service.uri}",
        configuration = CabAppFeignClientConfiguration.class)
public interface DirectPassengerClient extends PassengerClient {

    @Override
    @GetMapping("/{id}")
    PassengerResponseDto getPassengerById(@PathVariable String id);
}
