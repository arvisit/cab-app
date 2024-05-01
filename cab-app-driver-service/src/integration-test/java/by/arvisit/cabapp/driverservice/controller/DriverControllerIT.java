package by.arvisit.cabapp.driverservice.controller;

import static by.arvisit.cabapp.driverservice.util.DriverITData.JOHN_DOE_EMAIL;
import static by.arvisit.cabapp.driverservice.util.DriverITData.JOHN_DOE_ID_STRING;
import static by.arvisit.cabapp.driverservice.util.DriverITData.URL_AVAILABLE_DRIVERS;
import static by.arvisit.cabapp.driverservice.util.DriverITData.URL_DRIVERS;
import static by.arvisit.cabapp.driverservice.util.DriverITData.URL_DRIVERS_EMAIL_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverITData.URL_DRIVERS_ID_AVAILABILITY_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverITData.URL_DRIVERS_ID_TEMPLATE;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getAddedDriverResponse;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getJaneDoe;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getJannyDoe;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getJohnDoe;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getJohnnyDoe;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getListContainerForResponse;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getSaveDriverRequest;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getUpdateDriverRequest;
import static by.arvisit.cabapp.driverservice.util.DriverITData.getUpdatedDriverResponse;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import by.arvisit.cabapp.driverservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.driverservice.dto.DriverResponseDto;
import by.arvisit.cabapp.driverservice.persistence.model.Driver;
import by.arvisit.cabapp.driverservice.persistence.repository.DriverRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("itest")
@ExtendWith(PostgreSQLTestContainerExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-cars-drivers.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-cars-drivers.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
class DriverControllerIT {

    private static final String IS_AVAILABLE_PATCH_KEY = "isAvailable";
    private static final String ID_FIELD = "id";
    private static final String CAR_ID_FIELD = "car.id";
    private static final String VALUES_FIELD = "values";
    private static final String EMAIL_QUERY_PARAM = "email";
    private static final String NAME_QUERY_PARAM = "name";
    private static final String VALUE_FOR_EMAIL_QUERY_PARAM = "com";
    private static final String VALUE_FOR_NAME_QUERY_PARAM = "Doe";

    @LocalServerPort
    private int serverPort;
    @Autowired
    private DriverRepository driverRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn201AndExpectedResponse_whenSaveDriver() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(getSaveDriverRequest().build())
                .when().post(URL_DRIVERS);

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        DriverResponseDto result = response.as(DriverResponseDto.class);
        DriverResponseDto expected = getAddedDriverResponse().build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, CAR_ID_FIELD)
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenUpdateDriver() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(getUpdateDriverRequest().build())
                .when().put(URL_DRIVERS_ID_TEMPLATE, JOHN_DOE_ID_STRING);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverResponseDto result = response.as(DriverResponseDto.class);
        DriverResponseDto expected = getUpdatedDriverResponse().build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(CAR_ID_FIELD)
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenUpdateDriverAvailability() {
        boolean newValue = true;

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of(IS_AVAILABLE_PATCH_KEY, newValue))
                .when().patch(URL_DRIVERS_ID_AVAILABILITY_TEMPLATE, JOHN_DOE_ID_STRING);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverResponseDto result = response.as(DriverResponseDto.class);
        DriverResponseDto expected = getJohnDoe()
                .withIsAvailable(newValue)
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(CAR_ID_FIELD)
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn204AndMinusOneDriver_whenDeleteDriver() {

        List<Driver> driversBeforeDelete = driverRepository.findAll();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete(URL_DRIVERS_ID_TEMPLATE, JOHN_DOE_ID_STRING);

        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        List<Driver> driversAfterDelete = driverRepository.findAll();

        assertThat(driversAfterDelete).size()
                .isEqualTo(driversBeforeDelete.size() - 1);

        Predicate<? super Driver> matchById = p -> p.getId()
                .equals(UUID.fromString(JOHN_DOE_ID_STRING));
        assertThat(driversBeforeDelete)
                .anyMatch(matchById);
        assertThat(driversAfterDelete)
                .noneMatch(matchById);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetDriverById() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVERS_ID_TEMPLATE, JOHN_DOE_ID_STRING);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverResponseDto result = response.as(DriverResponseDto.class);
        DriverResponseDto expected = getJohnDoe().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetDriverByEmail() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVERS_EMAIL_TEMPLATE, JOHN_DOE_EMAIL);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        DriverResponseDto result = response.as(DriverResponseDto.class);
        DriverResponseDto expected = getJohnDoe().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetDriversWithNoRequestParams() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_DRIVERS);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<DriverResponseDto>>() {
                });
        ListContainerResponseDto<DriverResponseDto> expected = getListContainerForResponse(DriverResponseDto.class)
                .withValues(List.of(getJohnDoe().build(), getJaneDoe().build(),
                        getJohnnyDoe().build(), getJannyDoe().build()))
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(result.values())
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetAvailableDriversWithNoRequestParams() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_AVAILABLE_DRIVERS);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<DriverResponseDto>>() {
                });
        ListContainerResponseDto<DriverResponseDto> expected = getListContainerForResponse(DriverResponseDto.class)
                .withValues(List.of(getJohnnyDoe().build(), getJannyDoe().build()))
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(result.values())
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetDriversWithNameEmailRequestParams() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(NAME_QUERY_PARAM, VALUE_FOR_NAME_QUERY_PARAM)
                .queryParam(EMAIL_QUERY_PARAM, VALUE_FOR_EMAIL_QUERY_PARAM)
                .when().get(URL_DRIVERS);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<DriverResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<DriverResponseDto>>() {
                });
        ListContainerResponseDto<DriverResponseDto> expected = getListContainerForResponse(DriverResponseDto.class)
                .withValues(List.of(getJohnDoe().build(), getJaneDoe().build(), getJannyDoe().build()))
                .build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(result.values())
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }

}
