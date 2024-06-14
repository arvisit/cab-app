package by.arvisit.cabapp.jwtconverterstarter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import lombok.RequiredArgsConstructor;

@Configuration
@ConditionalOnProperty(prefix = "token.converter", name = "include", havingValue = "true")
@RequiredArgsConstructor
public class JwtConverterConfig {

    private final JwtConverterProperties jwtConverterProperties;

    @Bean
    @ConditionalOnMissingBean
    KeycloakJwtConverter keycloakJwtConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        return new KeycloakJwtConverter(jwtGrantedAuthoritiesConverter, jwtConverterProperties);
    }

}
