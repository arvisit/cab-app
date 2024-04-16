package by.arvisit.cabapp.ridesservice.messaging;

import java.util.function.Consumer;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import by.arvisit.cabapp.common.dto.payment.PassengerPaymentResponseDto;
import by.arvisit.cabapp.ridesservice.service.RideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MessagingConfiguration {

    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String PAYMENT_NOT_SUCCESS_STATUS_MESSAGE_TEMPLATE = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.isPaid.IllegalStateException.template";
    private final MessageSource messageSource; // TODO messageSource and success status are duplicates from
                                               // RideServiceImpl
                                               // should be extracted to some verification class
    private final RideService rideService;

    @Bean
    Consumer<PassengerPaymentResponseDto> confirmCardPayment() {
        return response -> {
            log.info("Consumer received response for card payment: {}", response);

            if (!SUCCESS_STATUS.equals(response.status())) {
                String errorMessage = messageSource.getMessage(
                        PAYMENT_NOT_SUCCESS_STATUS_MESSAGE_TEMPLATE,
                        new Object[] {}, null);
                throw new IllegalStateException(errorMessage);
            }

            rideService.confirmPayment(response.rideId());
        };
    }
}
