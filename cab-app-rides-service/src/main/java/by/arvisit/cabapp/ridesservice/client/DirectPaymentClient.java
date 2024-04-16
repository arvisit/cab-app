package by.arvisit.cabapp.ridesservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import by.arvisit.cabapp.common.dto.payment.PassengerPaymentRequestDto;
import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;

@FeignClient(name = "cab-app-payment-service", url = "${spring.settings.cab-app-payment-service.uri}",
        configuration = CabAppFeignClientConfiguration.class)
public interface DirectPaymentClient extends PaymentClient {

    @Override
    @PostMapping
    PassengerPaymentResponseDto save(@RequestBody PassengerPaymentRequestDto dto);

}
