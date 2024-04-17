package by.arvisit.cabapp.driverservice.messaging;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.rides.RideResponseDto;
import by.arvisit.cabapp.driverservice.client.RideClient;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MessagingConfiguration {

    private static final int SLEEP_BEFORE_DRIVER_SELECTION = 6;
    private static final String NO_AVAILABLE_DRIVERS_MESSAGE_TEMPLATE = "by.arvisit.cabapp.common.dto.ListContainerResponseDto.DriverResponseDto.IllegalStateException.template";
    private static final Random RANDOM = new Random();
    private final RideClient rideClient;
    private final DriverService driverService;
    private final MessageSource messageSource;

    @Bean
    Consumer<RideResponseDto> notifyDriversAboutNewRide() {
        return newRide -> {
            log.info("Consumer received message about new ride booked: {}", newRide);

            try {
                TimeUnit.SECONDS.sleep(SLEEP_BEFORE_DRIVER_SELECTION);

                ListContainerResponseDto<DriverResponseDto> availableDrivers = driverService
                        .getAvailableDrivers(Pageable.ofSize(10));

                if (availableDrivers.values().isEmpty()) {

                    String errorMessage = messageSource.getMessage(
                            NO_AVAILABLE_DRIVERS_MESSAGE_TEMPLATE,
                            new Object[] {}, null);
                    throw new IllegalStateException(errorMessage);
                }

                DriverResponseDto selectedDriver = availableDrivers.values()
                        .get(RANDOM.nextInt(0, availableDrivers.values().size()));
                rideClient.acceptRide(newRide.id(), Collections.singletonMap("driverId", selectedDriver.id()));
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        };
    }
}
