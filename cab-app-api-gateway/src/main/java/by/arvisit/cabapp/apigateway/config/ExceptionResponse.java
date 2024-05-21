package by.arvisit.cabapp.apigateway.config;

import java.time.ZonedDateTime;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record ExceptionResponse(
        int status,
        String message,
        ZonedDateTime timeStamp) {

}
