package by.arvisit.cabapp.driverservice.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import by.arvisit.cabapp.common.dto.rides.RideResponseDto;

@FeignClient(name = "cab-app-rides-service", url = "${spring.settings.cab-app-rides-service.uri}",
        configuration = CabAppFeignClientConfiguration.class)
public interface DirectRideClient extends RideClient {

    @Override
    @GetMapping("/{id}")
    RideResponseDto getRideById(@PathVariable String id);

    @Override
    @PatchMapping("/{id}/accept")
    RideResponseDto acceptRide(@PathVariable String id, @RequestBody Map<String, String> patch);
}
