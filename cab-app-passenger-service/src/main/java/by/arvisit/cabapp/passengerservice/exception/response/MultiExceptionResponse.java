package by.arvisit.cabapp.passengerservice.exception.response;

import java.time.ZonedDateTime;
import java.util.Map;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record MultiExceptionResponse(int status, Map<String, String> messages, ZonedDateTime timeStamp) {

}
