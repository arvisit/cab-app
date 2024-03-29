package by.arvisit.cabapp.passengerservice.service;

import by.arvisit.cabapp.passengerservice.dto.ListContainerResponseDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerRequestDto;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;

public interface PassengerService {

    ListContainerResponseDto<PassengerResponseDto> getPassengers();

    PassengerResponseDto getPassengerById(String id);

    PassengerResponseDto getPassengerByEmail(String email);

    PassengerResponseDto save(PassengerRequestDto dto);

    PassengerResponseDto update(String id, PassengerRequestDto dto);

    void delete(String id);

}
