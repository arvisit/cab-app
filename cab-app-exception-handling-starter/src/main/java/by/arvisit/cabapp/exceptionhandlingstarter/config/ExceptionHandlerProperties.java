package by.arvisit.cabapp.exceptionhandlingstarter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "exception.handling")
public class ExceptionHandlerProperties {

    private boolean include;

}
