package by.arvisit.cabapp.driverservice.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import by.arvisit.cabapp.common.dto.rides.RideResponseDto;

@Profile({ "dev" })
@FeignClient(name = "cab-app-rides-service", configuration = CabAppFeignClientConfiguration.class)
public interface DiscoveryRideClient extends RideClient {

    @Override
    @GetMapping("/api/v1/rides/{id}")
    RideResponseDto getRideById(@PathVariable String id);

    @Override
    @PatchMapping("/api/v1/rides/{id}/accept")
    RideResponseDto acceptRide(@PathVariable String id, @RequestBody Map<String, String> patch);
}
