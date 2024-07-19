package by.arvisit.cabapp.ridesservice.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.internal.http.URIBuilder;
import io.restassured.response.Response;

public abstract class KeycloakE2ETestAuth {

    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String PASSWORD_KEY = "password";
    public static final String USERNAME_KEY = "username";
    public static final String CLIENT_SECRET_KEY = "client_secret";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String GRANT_TYPE_KEY = "grant_type";

    private static final String CLIENT_SECRET = "wZpxwHzxVfPmqi46Ck5BUD6wxMD5QlSe";
    private static final String CLIENT_ID = "cab-app";
    private static final String AUTH_URL = "http://localhost:8180/realms/cab-app-realm/protocol/openid-connect/token";

    protected Header getAuthenticationHeader(String username, String password) {
        URI authenticationUri;

        try {
            authenticationUri = URIBuilder.convertToURI(AUTH_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Malformed URI");
        }

        int keycloakPort = authenticationUri.getPort();
        String authPath = authenticationUri.getPath();

        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam(GRANT_TYPE_KEY, "password")
                .formParam(CLIENT_ID_KEY, CLIENT_ID)
                .formParam(CLIENT_SECRET_KEY, CLIENT_SECRET)
                .formParam(USERNAME_KEY, username)
                .formParam(PASSWORD_KEY, password)
                .port(keycloakPort)
                .when().post(authPath);

        if (response.statusCode() != HttpStatus.OK.value()) {
            throw new RuntimeException("Failed to get token: " + response.statusLine());
        }

        Map<String, String> responseBody = response.as(new TypeRef<Map<String, String>>() {
        });
        String token = responseBody.get(KeycloakE2ETestAuth.ACCESS_TOKEN_KEY);

        return new Header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
