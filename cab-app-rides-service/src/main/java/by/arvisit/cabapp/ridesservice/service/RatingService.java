package by.arvisit.cabapp.ridesservice.service;

import by.arvisit.cabapp.ridesservice.dto.RatingResponseDto;

public interface RatingService {

    RatingResponseDto getPassengerRating(String id);

    RatingResponseDto getDriverRating(String id);
}
