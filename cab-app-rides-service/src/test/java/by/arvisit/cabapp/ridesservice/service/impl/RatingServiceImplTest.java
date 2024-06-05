package by.arvisit.cabapp.ridesservice.service.impl;

import static by.arvisit.cabapp.ridesservice.util.RideTestData.DEFAULT_RATING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_DRIVER_ID_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.RIDE_DEFAULT_PASSENGER_ID_STRING;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getDriverRating;
import static by.arvisit.cabapp.ridesservice.util.RideTestData.getPassengerRating;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import by.arvisit.cabapp.ridesservice.dto.RatingResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.repository.RideRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @InjectMocks
    private RatingServiceImpl ratingService;
    @Mock
    private RideRepository rideRepository;
    @Mock
    private MessageSource messageSource;

    @Test
    void shouldReturnRating_whenGetPassengerRatingForExistingPassenger() {
        when(rideRepository.getPassengerAverageScore(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(DEFAULT_RATING));

        RatingResponseDto actual = ratingService.getPassengerRating(RIDE_DEFAULT_PASSENGER_ID_STRING);
        RatingResponseDto expected = getPassengerRating().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetPassengerRatingForNonExistingPassenger() {
        when(rideRepository.getPassengerAverageScore(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.getPassengerRating(RIDE_DEFAULT_PASSENGER_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldReturnRating_whenGetDriverRatingForExistingDriver() {
        when(rideRepository.getDriverAverageScore(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(DEFAULT_RATING));

        RatingResponseDto actual = ratingService.getDriverRating(RIDE_DEFAULT_DRIVER_ID_STRING);
        RatingResponseDto expected = getDriverRating().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenGetDriverRatingForNonExistingDriver() {
        when(rideRepository.getDriverAverageScore(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.getDriverRating(RIDE_DEFAULT_DRIVER_ID_STRING))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
