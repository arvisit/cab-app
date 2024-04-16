package by.arvisit.cabapp.paymentservice.messaging;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentRequestDto;
import by.arvisit.cabapp.paymentservice.service.PassengerPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MessagingConfiguration {

    private final PassengerPaymentService passengerPaymentService;

    @Bean
    Consumer<PassengerPaymentRequestDto> createPayment() {
        return request -> {
            log.info("Consumer received request for payment: {}", request);
            passengerPaymentService.save(request);
        };
    }
}
