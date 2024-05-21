package by.arvisit.cabapp.ridesservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import by.arvisit.cabapp.common.dto.payment.PassengerPaymentRequestDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Profile({ "dev" })
@FeignClient(name = "cab-app-payment-service", configuration = CabAppFeignClientConfiguration.class)
@CircuitBreaker(name = "discoveryPaymentClient")
public interface DiscoveryPaymentClient extends PaymentClient {

    @Override
    @PostMapping("/api/v1/passenger-payments")
    PassengerPaymentResponseDto save(@RequestBody PassengerPaymentRequestDto dto);

}
