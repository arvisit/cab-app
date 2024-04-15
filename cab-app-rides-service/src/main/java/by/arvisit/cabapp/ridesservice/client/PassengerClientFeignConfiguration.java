package by.arvisit.cabapp.ridesservice.client;

import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;

@Configuration
public class PassengerClientFeignConfiguration extends FeignClientConfiguration {

    @Bean
    ErrorDecoder errorDecoder() {
        return new PassengerClientErrorDecoder();
    }
}
