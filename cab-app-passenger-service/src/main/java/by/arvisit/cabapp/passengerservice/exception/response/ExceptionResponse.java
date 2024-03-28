package by.arvisit.cabapp.passengerservice.exception.response;

import java.time.ZonedDateTime;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record ExceptionResponse(int status, String message, ZonedDateTime timeStamp) {

}
