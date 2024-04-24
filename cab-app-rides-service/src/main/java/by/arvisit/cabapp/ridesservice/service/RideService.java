package by.arvisit.cabapp.ridesservice.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.ridesservice.dto.RideRequestDto;
import by.arvisit.cabapp.ridesservice.dto.RideResponseDto;

public interface RideService {

    RideResponseDto save(RideRequestDto dto);

    RideResponseDto cancelRide(String id);

    RideResponseDto acceptRide(String rideId, String driverId);

    RideResponseDto beginRide(String id);

    RideResponseDto endRide(String id);

    RideResponseDto finishRide(String id);

    RideResponseDto confirmPayment(String id);

    RideResponseDto applyPromoCode(String rideId, String promoCodeKeyword);

    RideResponseDto changePaymentMethod(String rideId, String paymentMethod);

    RideResponseDto scoreDriver(String rideId, Integer score);

    RideResponseDto scorePassenger(String rideId, Integer score);

    void delete(String id);

    RideResponseDto getRideById(String id);

    ListContainerResponseDto<RideResponseDto> getRides(Pageable pageable, Map<String, String> params);

    ListContainerResponseDto<RideResponseDto> getRidesByPassengerId(String id, Pageable pageable);

    ListContainerResponseDto<RideResponseDto> getRidesByDriverId(String id, Pageable pageable);

}
