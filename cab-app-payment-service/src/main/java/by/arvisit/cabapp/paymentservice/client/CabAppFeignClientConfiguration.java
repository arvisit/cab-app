package by.arvisit.cabapp.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClientProperties.FeignClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;

@Configuration
public class CabAppFeignClientConfiguration extends FeignClientConfiguration {

    @Bean
    ErrorDecoder errorDecoder() {
        return new CabAppErrorDecoder();
    }
}
