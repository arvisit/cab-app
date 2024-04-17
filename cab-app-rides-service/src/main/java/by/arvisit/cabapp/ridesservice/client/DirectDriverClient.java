package by.arvisit.cabapp.ridesservice.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;

@FeignClient(name = "cab-app-driver-service", url = "${spring.settings.cab-app-driver-service.uri}",
        configuration = CabAppFeignClientConfiguration.class)
public interface DirectDriverClient extends DriverClient {

    @Override
    @GetMapping("/{id}")
    DriverResponseDto getDriverById(@PathVariable String id);

    @Override
    @PatchMapping("/{id}/availability")
    DriverResponseDto updateAvailability(@PathVariable String id, @RequestBody Map<String, Boolean> patch);

}
