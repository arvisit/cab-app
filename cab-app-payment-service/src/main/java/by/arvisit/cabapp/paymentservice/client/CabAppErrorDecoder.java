package by.arvisit.cabapp.paymentservice.client;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import by.arvisit.cabapp.exceptionhandlingstarter.exception.BadRequestException;
import by.arvisit.cabapp.exceptionhandlingstarter.response.ExceptionResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.persistence.EntityNotFoundException;

public class CabAppErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        ExceptionResponse message = null;
        try (InputStream bodyIs = response.body().asInputStream()) {

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            message = objectMapper.readValue(bodyIs, ExceptionResponse.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        if (message.status() == 400) {
            throw new BadRequestException(message.message());
        }
        if (message.status() == 404) {
            throw new EntityNotFoundException(message.message());
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }

}
