package by.arvisit.cabapp.ridesservice.client;

import by.arvisit.cabapp.ridesservice.dto.PassengerResponseDto;

public interface PassengerClient {

    PassengerResponseDto getPassengerById(String id);
}
