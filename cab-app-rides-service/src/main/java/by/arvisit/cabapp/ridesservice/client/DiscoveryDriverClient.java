package by.arvisit.cabapp.ridesservice.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;

@Profile({ "dev" })
@FeignClient(name = "cab-app-driver-service", configuration = CabAppFeignClientConfiguration.class)
public interface DiscoveryDriverClient extends DriverClient {

    @Override
    @GetMapping("/api/v1/drivers/{id}")
    DriverResponseDto getDriverById(@PathVariable String id);

    @Override
    @PatchMapping("/api/v1/drivers/{id}/availability")
    DriverResponseDto updateAvailability(@PathVariable String id, @RequestBody Map<String, Boolean> patch);

}
