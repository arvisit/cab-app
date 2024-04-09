package by.arvisit.cabapp.exceptionhandlingstarter.handler;

import java.net.ConnectException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import by.arvisit.cabapp.exceptionhandlingstarter.exception.BadRequestException;
import by.arvisit.cabapp.exceptionhandlingstarter.exception.UsernameAlreadyExistsException;
import by.arvisit.cabapp.exceptionhandlingstarter.response.ExceptionResponse;
import by.arvisit.cabapp.exceptionhandlingstarter.response.MultiExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    private static final ZoneId EUROPE_MINSK_TIMEZONE = ZoneId.of("Europe/Minsk");

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handle(EntityNotFoundException exception) {
        return ExceptionResponse.builder()
                .withStatus(HttpStatus.NOT_FOUND.value())
                .withMessage(exception.getMessage())
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handle(IllegalArgumentException exception) {
        return ExceptionResponse.builder()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withMessage(exception.getMessage())
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ExceptionResponse handle(HttpRequestMethodNotSupportedException exception) {
        return ExceptionResponse.builder()
                .withStatus(HttpStatus.METHOD_NOT_ALLOWED.value())
                .withMessage(exception.getMessage())
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handle(HttpMessageNotReadableException exception) {
        return ExceptionResponse.builder()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withMessage("Request body is missing or could not be read")
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handle(BadRequestException exception) {
        return ExceptionResponse.builder()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withMessage(exception.getMessage())
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

    @ExceptionHandler(ConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handle(ConnectException exception) {
        return ExceptionResponse.builder()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .withMessage(exception.getMessage())
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handle(UsernameAlreadyExistsException exception) {
        return ExceptionResponse.builder()
                .withStatus(HttpStatus.CONFLICT.value())
                .withMessage(exception.getMessage())
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MultiExceptionResponse handle(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    String fieldName = error.getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return MultiExceptionResponse.builder()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withMessages(errors)
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MultiExceptionResponse handle(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations()
                .forEach(constraintViolation -> constraintViolation.getPropertyPath()
                        .forEach(error -> errors.put(constraintViolation.getMessage(), error.getName())));
        return MultiExceptionResponse.builder()
                .withStatus(HttpStatus.BAD_REQUEST.value())
                .withMessages(errors)
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }
}
