package by.arvisit.cabapp.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import by.arvisit.cabapp.common.dto.driver.DriverResponseDto;

@Profile({ "dev", "itest" })
@FeignClient(name = "cab-app-driver-service", url = "${spring.settings.cab-app-driver-service.uri}",
        configuration = CabAppFeignClientConfiguration.class)
public interface DirectDriverClient extends DriverClient {

    @Override
    @GetMapping("/{id}")
    DriverResponseDto getDriverById(@PathVariable String id);

}
