package by.arvisit.cabapp.jwtconverterstarter.config;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import by.arvisit.cabapp.common.security.AppUser;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String EMAIL_CLAIM = "email";
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
    private final JwtConverterProperties properties;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        AppUser user = extractUserInfo(jwt);
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    private AppUser extractUserInfo(Jwt jwt) {
        return AppUser.builder()
                .withAuthorities(extractAuthorities(jwt))
                .withEmail(jwt.getClaimAsString(EMAIL_CLAIM))
                .withId(jwt.getClaimAsString(JwtClaimNames.SUB))
                .withEnabled(true)
                .withPassword(null)
                .withUsername(jwt.getClaimAsString(properties.getPrincipalAttribute()))
                .build();
    }

    @SuppressWarnings("unchecked")
    private Collection<? extends GrantedAuthority> extractAuthorities(Jwt jwt) {
        Stream<SimpleGrantedAuthority> accesses = Optional.of(jwt)
                .map(token -> token.getClaimAsMap(RESOURCE_ACCESS))
                .map(claimMap -> (Map<String, Object>) claimMap.get(properties.getResourceId()))
                .map(resourceData -> (Collection<String>) resourceData.get(ROLES))
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .distinct();
        return Stream
                .concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), accesses)
                .collect(Collectors.toSet());
    }
}
