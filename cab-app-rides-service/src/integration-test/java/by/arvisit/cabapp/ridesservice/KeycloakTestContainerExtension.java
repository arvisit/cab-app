package by.arvisit.cabapp.ridesservice;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import dasniko.testcontainers.keycloak.KeycloakContainer;

public class KeycloakTestContainerExtension implements BeforeAllCallback, AfterAllCallback {

    public static final String JWK_SET_URI_PROPERTY_KEY = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri";
    public static final String TOKEN_URI_PROPERTY_KEY = "spring.security.oauth2.client.provider.keycloak.token-uri";
    public static final String KEYCLOAK_SERVER_URL_PROPERTY_KEY = "security.keycloak.server.url";

    static KeycloakContainer keycloakContainer;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (System.getProperty(KEYCLOAK_SERVER_URL_PROPERTY_KEY) == null) {
            keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:24.0")
                    .withRealmImportFile("/test-realm.json");
            keycloakContainer.start();
            System.setProperty(KEYCLOAK_SERVER_URL_PROPERTY_KEY, keycloakContainer.getAuthServerUrl());
            System.setProperty(TOKEN_URI_PROPERTY_KEY,
                    keycloakContainer.getAuthServerUrl() + "/realms/cab-app-realm/protocol/openid-connect/token");
            System.setProperty(JWK_SET_URI_PROPERTY_KEY,
                    keycloakContainer.getAuthServerUrl() + "/realms/cab-app-realm/protocol/openid-connect/certs");
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
    }

}
