package by.arvisit.cabapp.ridesservice.controller;

import static by.arvisit.cabapp.ridesservice.KeycloakTestContainerExtension.ACCESS_TOKEN_KEY;
import static by.arvisit.cabapp.ridesservice.KeycloakTestContainerExtension.CLIENT_ID_KEY;
import static by.arvisit.cabapp.ridesservice.KeycloakTestContainerExtension.CLIENT_SECRET_KEY;
import static by.arvisit.cabapp.ridesservice.KeycloakTestContainerExtension.GRANT_TYPE_KEY;
import static by.arvisit.cabapp.ridesservice.KeycloakTestContainerExtension.PASSWORD_KEY;
import static by.arvisit.cabapp.ridesservice.KeycloakTestContainerExtension.TOKEN_URI_PROPERTY_KEY;
import static by.arvisit.cabapp.ridesservice.KeycloakTestContainerExtension.USERNAME_KEY;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.ADMIN_EMAIL;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.ADMIN_PASSWORD;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.BRILLIANT10_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.NEW_PROMO_CODE_DISCOUNT_PERCENT;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.NEW_PROMO_CODE_KEYWORD;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_PROMO_CODES;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_PROMO_CODES_ACTIVE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_PROMO_CODES_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getAddedPromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getBRILLIANT10ActivePromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getListContainerForResponse;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getNewPromoCodeRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getPAIN49ActivePromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getPAIN49NotActivePromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getRICE23NotActivePromoCodeResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.ridesservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.ridesservice.KeycloakTestContainerExtension;
import by.arvisit.cabapp.ridesservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;
import by.arvisit.cabapp.ridesservice.persistence.repository.PromoCodeRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@ExtendWith(KeycloakTestContainerExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-promo-codes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-promo-codes.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
class PromoCodeControllerIntegrationTest {

    private static final String ID_FIELD = "id";
    private static final String VALUES_FIELD = "values";

    @LocalServerPort
    private int serverPort;
    @Autowired
    private PromoCodeRepository promoCodeRepository;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    private Header getAuthenticationHeader(String username, String password) {
        Client client = ClientBuilder.newClient();
        String authUrl = System.getProperty(TOKEN_URI_PROPERTY_KEY);

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.put(GRANT_TYPE_KEY, Collections.singletonList("password"));
        formData.put(CLIENT_ID_KEY, Collections.singletonList(clientId));
        formData.put(CLIENT_SECRET_KEY, Collections.singletonList(clientSecret));
        formData.put(USERNAME_KEY, Collections.singletonList(username));
        formData.put(PASSWORD_KEY, Collections.singletonList(password));

        jakarta.ws.rs.core.Response response = client.target(authUrl)
                .request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(Entity.form(new Form(formData)));

        if (response.getStatus() != HttpStatus.OK.value()) {
            throw new RuntimeException("Failed to get token: " + response.getStatus());
        }

        Map<String, String> responseBody = response.readEntity(new GenericType<Map<String, String>>() {
        });
        String token = responseBody.get(ACCESS_TOKEN_KEY);

        return new Header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    @Test
    void shouldReturn201AndExpectedResponse_whenSavePromoCode() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(getAuthenticationHeader(ADMIN_EMAIL, ADMIN_PASSWORD))
                .body(getNewPromoCodeRequestDto().build())
                .when().post(URL_PROMO_CODES);

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto result = response.as(PromoCodeResponseDto.class);
        PromoCodeResponseDto expected = getAddedPromoCodeResponseDto().build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD)
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenUpdatePromoCode() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(getAuthenticationHeader(ADMIN_EMAIL, ADMIN_PASSWORD))
                .body(getNewPromoCodeRequestDto().build())
                .when().put(URL_PROMO_CODES_ID_TEMPLATE, BRILLIANT10_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto result = response.as(PromoCodeResponseDto.class);
        PromoCodeResponseDto expected = getBRILLIANT10ActivePromoCodeResponseDto()
                .withKeyword(NEW_PROMO_CODE_KEYWORD)
                .withDiscountPercent(NEW_PROMO_CODE_DISCOUNT_PERCENT)
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenDeactivatePromoCode() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(getAuthenticationHeader(ADMIN_EMAIL, ADMIN_PASSWORD))
                .body(getNewPromoCodeRequestDto().build())
                .when().patch(URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE, BRILLIANT10_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto result = response.as(PromoCodeResponseDto.class);
        PromoCodeResponseDto expected = getBRILLIANT10ActivePromoCodeResponseDto()
                .withIsActive(false)
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn204AndMinusOnePromoCode_whenDeletePromoCode() {
        List<PromoCode> promoCodesBeforeDelete = promoCodeRepository.findAll();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(getAuthenticationHeader(ADMIN_EMAIL, ADMIN_PASSWORD))
                .when().delete(URL_PROMO_CODES_ID_TEMPLATE, BRILLIANT10_ID);

        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        List<PromoCode> promoCodesAfterDelete = promoCodeRepository.findAll();

        assertThat(promoCodesAfterDelete).size()
                .isEqualTo(promoCodesBeforeDelete.size() - 1);

        Predicate<? super PromoCode> matchById = p -> p.getId()
                .equals(BRILLIANT10_ID);
        assertThat(promoCodesBeforeDelete)
                .anyMatch(matchById);
        assertThat(promoCodesAfterDelete)
                .noneMatch(matchById);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPromoCodeById() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(getAuthenticationHeader(ADMIN_EMAIL, ADMIN_PASSWORD))
                .body(getNewPromoCodeRequestDto().build())
                .when().get(URL_PROMO_CODES_ID_TEMPLATE, BRILLIANT10_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto result = response.as(PromoCodeResponseDto.class);
        PromoCodeResponseDto expected = getBRILLIANT10ActivePromoCodeResponseDto().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPromoCodes() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(getAuthenticationHeader(ADMIN_EMAIL, ADMIN_PASSWORD))
                .when().get(URL_PROMO_CODES);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PromoCodeResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<PromoCodeResponseDto>>() {
                });
        ListContainerResponseDto<PromoCodeResponseDto> expected = getListContainerForResponse(
                PromoCodeResponseDto.class)
                .withValues(List.of(getBRILLIANT10ActivePromoCodeResponseDto().build(),
                        getPAIN49ActivePromoCodeResponseDto().build(),
                        getPAIN49NotActivePromoCodeResponseDto().build(),
                        getRICE23NotActivePromoCodeResponseDto().build()))
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(result.values())
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetActivePromoCodes() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header(getAuthenticationHeader(ADMIN_EMAIL, ADMIN_PASSWORD))
                .when().get(URL_PROMO_CODES_ACTIVE);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PromoCodeResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<PromoCodeResponseDto>>() {
                });
        ListContainerResponseDto<PromoCodeResponseDto> expected = getListContainerForResponse(
                PromoCodeResponseDto.class)
                .withValues(List.of(getBRILLIANT10ActivePromoCodeResponseDto().build(),
                        getPAIN49ActivePromoCodeResponseDto().build()))
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(result.values())
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }
}
