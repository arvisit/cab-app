package by.arvisit.cabapp.ridesservice.messaging;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;
import by.arvisit.cabapp.ridesservice.service.RideService;
import by.arvisit.cabapp.ridesservice.util.PaymentVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MessagingConfiguration {

    private final RideService rideService;
    private final PaymentVerifier paymentVerifier;

    @Bean
    Consumer<PassengerPaymentResponseDto> confirmCardPayment() {
        return response -> {
            log.info("Consumer received response for card payment: {}", response);

            paymentVerifier.verifyPaymentStatus(response);
            rideService.confirmPayment(response.rideId());
        };
    }
}
