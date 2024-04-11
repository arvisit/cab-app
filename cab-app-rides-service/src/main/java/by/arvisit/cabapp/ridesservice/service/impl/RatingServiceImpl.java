package by.arvisit.cabapp.ridesservice.service.impl;

import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import by.arvisit.cabapp.ridesservice.dto.RatingResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.UserTypeEnum;
import by.arvisit.cabapp.ridesservice.persistence.repository.RideRepository;
import by.arvisit.cabapp.ridesservice.service.RatingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImpl implements RatingService {

    private static final String FOUND_NO_RATING_BY_PASSENGER_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.passengerScore.EntityNotFoundException.template";
    private static final String FOUND_NO_RATING_BY_DRIVER_ID_MESSAGE_TEMPLATE_KEY = "by.arvisit.cabapp.ridesservice.persistence.model.Ride.driverScore.EntityNotFoundException.template";

    private final RideRepository rideRepository;
    private final MessageSource messageSource;

    @Override
    public RatingResponseDto getPassengerRating(String id) {
        log.debug("Call for RatingService.getPassengerRating() with id {}", id);

        UUID uuid = UUID.fromString(id);
        String errorMessage = messageSource.getMessage(
                FOUND_NO_RATING_BY_PASSENGER_ID_MESSAGE_TEMPLATE_KEY,
                new Object[] { id }, null);
        Double rating = rideRepository.getPassengerAverageScore(uuid)
                .orElseThrow(() -> new EntityNotFoundException(errorMessage));

        return RatingResponseDto.builder()
                .withUserType(UserTypeEnum.PASSENGER)
                .withUserId(id)
                .withRating(rating)
                .build();
    }

    @Override
    public RatingResponseDto getDriverRating(String id) {
        log.debug("Call for RatingService.getDriverRating() with id {}", id);

        UUID uuid = UUID.fromString(id);
        String errorMessage = messageSource.getMessage(
                FOUND_NO_RATING_BY_DRIVER_ID_MESSAGE_TEMPLATE_KEY,
                new Object[] { id }, null);
        Double rating = rideRepository.getDriverAverageScore(uuid)
                .orElseThrow(() -> new EntityNotFoundException(errorMessage));

        return RatingResponseDto.builder()
                .withUserType(UserTypeEnum.DRIVER)
                .withUserId(id)
                .withRating(rating)
                .build();
    }

}
