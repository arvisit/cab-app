package by.arvisit.cabapp.ridesservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import by.arvisit.cabapp.ridesservice.dto.PassengerResponseDto;

@FeignClient(value = "cab-app-passenger-service", url = "${spring.settings.cab-app-passenger-service.uri}",
        configuration = CabAppFeignClientConfiguration.class)
public interface DirectPassengerClient extends PassengerClient {

    @Override
    @GetMapping("/{id}")
    PassengerResponseDto getPassengerById(@PathVariable String id);
}
