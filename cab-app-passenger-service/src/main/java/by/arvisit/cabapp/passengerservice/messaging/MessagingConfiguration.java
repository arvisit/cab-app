package by.arvisit.cabapp.passengerservice.messaging;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import by.arvisit.cabapp.common.dto.rides.RideResponseDto;
import lombok.extern.slf4j.Slf4j;

@Profile("!itest")
@Configuration
@Slf4j
public class MessagingConfiguration {

    @Bean
    Consumer<RideResponseDto> notifyAboutRideAcceptance() {
        return ride -> {
            log.info("Consumer received message about ride acceptance: {}", ride);
        };
    }
}
