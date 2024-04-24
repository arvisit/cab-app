package by.arvisit.cabapp.passengerservice.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;

public interface PassengerService {

    ListContainerResponseDto<PassengerResponseDto> getPassengers(Pageable pageable, Map<String, String> params);

    PassengerResponseDto getPassengerById(String id);

    PassengerResponseDto getPassengerByEmail(String email);

    PassengerResponseDto save(PassengerRequestDto dto);

    PassengerResponseDto update(String id, PassengerRequestDto dto);

    void delete(String id);

}
