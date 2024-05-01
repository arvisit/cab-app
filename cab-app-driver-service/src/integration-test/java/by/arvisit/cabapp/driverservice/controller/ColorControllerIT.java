package by.arvisit.cabapp.driverservice.controller;

import static by.arvisit.cabapp.driverservice.util.ColorITData.COLORS;
import static by.arvisit.cabapp.driverservice.util.DriverITData.URL_COLORS;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.driverservice.dto.ColorResponseDto;
import by.arvisit.cabapp.driverservice.util.DriverITData;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("itest")
@ExtendWith(PostgreSQLTestContainerExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ColorControllerIT {

    private static final String VALUES_FIELD = "values";

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn200_whenGetColors() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_COLORS);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<ColorResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<ColorResponseDto>>() {
                });
        ListContainerResponseDto<ColorResponseDto> expected = DriverITData
                .getListContainerForResponse(ColorResponseDto.class)
                .withValues(COLORS)
                .withLastPage(1)
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(result.values())
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }
}
