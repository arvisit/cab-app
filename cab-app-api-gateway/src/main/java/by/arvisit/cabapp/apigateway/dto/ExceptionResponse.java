package by.arvisit.cabapp.apigateway.dto;

import java.time.ZonedDateTime;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record ExceptionResponse(
        int status,
        String message,
        ZonedDateTime timeStamp) {

}
