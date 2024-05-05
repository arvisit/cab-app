package by.arvisit.cabapp.ridesservice.controller;

import static by.arvisit.cabapp.ridesservice.util.RideITData.BRILLIANT10_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.NEW_PROMO_CODE_DISCOUNT_PERCENT;
import static by.arvisit.cabapp.ridesservice.util.RideITData.NEW_PROMO_CODE_KEYWORD;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_PROMO_CODES;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_PROMO_CODES_ACTIVE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_PROMO_CODES_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getAddedPromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getBRILLIANT10ActivePromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getListContainerForResponse;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getNewPromoCodeRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getPAIN49ActivePromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getPAIN49NotActivePromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getRICE23NotActivePromoCodeResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.ridesservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.ridesservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;
import by.arvisit.cabapp.ridesservice.persistence.repository.PromoCodeRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-promo-codes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-promo-codes.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
class PromoCodeControllerIT {

    private static final String ID_FIELD = "id";
    private static final String VALUES_FIELD = "values";

    @LocalServerPort
    private int serverPort;
    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn201AndExpectedResponse_whenSavePromoCode() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
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
