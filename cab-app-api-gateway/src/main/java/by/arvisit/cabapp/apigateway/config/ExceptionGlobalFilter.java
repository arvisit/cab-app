package by.arvisit.cabapp.apigateway.config;

import java.net.ConnectException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServiceUnavailableException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class ExceptionGlobalFilter implements GlobalFilter, Ordered {

    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    private static final String SERVICE_IS_TEMPORARY_UNAVAILABLE_MESSAGE = "Service is temporary unavailable. Try again later";
    private static final ZoneId EUROPE_MINSK_TIMEZONE = ZoneId.of("Europe/Minsk");

    private final ObjectMapper objectMapper;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).onErrorResume(ex -> {
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            log.error(ex.getMessage());

            ExceptionResponse exceptionResponse;
            if (ex instanceof ResponseStatusException responseStatusException) {
                exchange.getResponse().setStatusCode(responseStatusException.getStatusCode());
                exceptionResponse = handle(responseStatusException);
            } else if (ex instanceof ConnectException connectException) {
                exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                exceptionResponse = handle(connectException);
            } else if (ex instanceof ServiceUnavailableException serviceUnavailableException) {
                exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                exceptionResponse = handle(serviceUnavailableException);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                exceptionResponse = handle(ex);
            }

            byte[] bytes = new byte[0];
            try {
                bytes = objectMapper.writeValueAsBytes(exceptionResponse);
            } catch (JsonProcessingException e) {
                log.error("Exception while serializing ExceptionResponse: {}", exceptionResponse);
            }

            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        });
    }

    private ExceptionResponse handle(ResponseStatusException responseStatusException) {
        ExceptionResponse.ExceptionResponseBuilder builder = ExceptionResponse.builder();

        if (responseStatusException.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            builder.withMessage(SERVICE_IS_TEMPORARY_UNAVAILABLE_MESSAGE);
        } else {
            builder.withMessage(responseStatusException.getReason());
        }
        return builder
                .withStatus(responseStatusException.getStatusCode().value())
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

    private ExceptionResponse handle(ConnectException connectException) {
        return ExceptionResponse.builder()
                .withStatus(HttpStatus.SERVICE_UNAVAILABLE.value())
                .withMessage(SERVICE_IS_TEMPORARY_UNAVAILABLE_MESSAGE)
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

    private ExceptionResponse handle(ServiceUnavailableException serviceUnavailableException) {
        return ExceptionResponse.builder()
                .withStatus(HttpStatus.SERVICE_UNAVAILABLE.value())
                .withMessage(SERVICE_IS_TEMPORARY_UNAVAILABLE_MESSAGE)
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

    private ExceptionResponse handle(Throwable exception) {
        return ExceptionResponse.builder()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .withMessage(INTERNAL_SERVER_ERROR_MESSAGE)
                .withTimeStamp(ZonedDateTime.now(EUROPE_MINSK_TIMEZONE))
                .build();
    }

}
