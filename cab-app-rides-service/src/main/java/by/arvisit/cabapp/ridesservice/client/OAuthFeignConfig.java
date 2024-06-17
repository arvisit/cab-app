package by.arvisit.cabapp.ridesservice.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.cloud.openfeign.security.OAuth2AccessTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuthFeignConfig {
    private final OAuth2ClientProperties oAuth2ClientProperties;

    @Bean
    @ConditionalOnBean({ OAuth2AuthorizedClientService.class, ClientRegistrationRepository.class })
    @ConditionalOnMissingBean
    OAuth2AuthorizedClientManager feignOAuth2AuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository,
                oAuth2AuthorizedClientService);
    }

    @Bean
    @ConditionalOnBean(OAuth2AuthorizedClientManager.class)
    OAuth2AccessTokenInterceptor defaultOAuth2AccessTokenInterceptor(
            OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        return new OAuth2AccessTokenInterceptor(oAuth2ClientProperties.getRegistration().keySet().iterator().next(),
                oAuth2AuthorizedClientManager);
    }
}
