package by.arvisit.cabapp.jwtconverterstarter.config;

import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "token.converter")
public class JwtConverterProperties {

    private boolean include;
    private String resourceId;
    private String principalAttribute;

    public Optional<String> getPrincipalAttribute() {
        return Optional.ofNullable(principalAttribute);
    }
}
