package by.arvisit.cabapp.driverservice.controller;

import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.JOHN_DOE_CAR_ID_STRING;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_CARS;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.URL_CARS_ID_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJaneDoeCar;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJannyDoeCar;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJohnDoeCar;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getJohnnyDoeCar;
import static by.arvisit.cabapp.driverservice.util.DriverIntegrationTestData.getListContainerForResponse;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.driverservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.driverservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.driverservice.dto.CarResponseDto;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("itest")
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-cars-drivers.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-cars-drivers.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
class CarControllerIntegrationTest {

    private static final String VALUES_FIELD = "values";

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn200_whenGetCars() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_CARS);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<CarResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<CarResponseDto>>() {
                });
        ListContainerResponseDto<CarResponseDto> expected = getListContainerForResponse(CarResponseDto.class)
                .withValues(List.of(getJaneDoeCar().build(), getJannyDoeCar().build(), getJohnnyDoeCar().build(),
                        getJohnDoeCar().build()))
                .withLastPage(0)
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(result.values())
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetCarById() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_CARS_ID_TEMPLATE, JOHN_DOE_CAR_ID_STRING);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        CarResponseDto result = response.as(CarResponseDto.class);
        CarResponseDto expected = getJohnDoeCar().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
