package by.arvisit.cabapp.paymentservice.client;

import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;

public interface PassengerClient {

    PassengerResponseDto getPassengerById(String id);
}
