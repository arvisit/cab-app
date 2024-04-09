package by.arvisit.cabapp.exceptionhandlingstarter.response;

import java.time.ZonedDateTime;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record ExceptionResponse(
        int status,
        String message,
        ZonedDateTime timeStamp) {

}
