package by.arvisit.cabapp.exceptionhandlingstarter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import by.arvisit.cabapp.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;

@Configuration
@ConditionalOnProperty(prefix = "exception.handling", name = "include", havingValue = "true")
public class ExceptionHandlerConfig {

    @Bean
    @ConditionalOnMissingBean
    GlobalExceptionHandlerAdvice globalHandlerAdvice() {
        return new GlobalExceptionHandlerAdvice();
    }

}
