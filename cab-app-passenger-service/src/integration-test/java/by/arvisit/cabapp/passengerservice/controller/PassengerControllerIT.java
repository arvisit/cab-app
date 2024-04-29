package by.arvisit.cabapp.passengerservice.controller;

import static by.arvisit.cabapp.passengerservice.util.PassengerITData.JOHN_DOE_EMAIL;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.JOHN_DOE_ID_STRING;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.URL_PASSENGERS;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.URL_PASSENGERS_EMAIL_TEMPLATE;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.URL_PASSENGERS_ID_TEMPLATE;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getAddedPassengerResponse;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getJaneDoe;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getJannyDoe;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getJohnDoe;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getJohnnyDoe;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getListContainerForPassengers;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getSavePassengerRequest;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getUpdatePassengerRequest;
import static by.arvisit.cabapp.passengerservice.util.PassengerITData.getUpdatedPassengerResponse;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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
import by.arvisit.cabapp.passengerservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.passengerservice.dto.PassengerResponseDto;
import by.arvisit.cabapp.passengerservice.persistence.model.Passenger;
import by.arvisit.cabapp.passengerservice.persistence.repository.PassengerRepository;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-passengers.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-passengers.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
class PassengerControllerIT {

    private static final String ID_FIELD = "id";
    private static final String VALUES_FIELD = "values";
    private static final String EMAIL_QUERY_PARAM = "email";
    private static final String NAME_QUERY_PARAM = "name";
    private static final String VALUE_FOR_EMAIL_QUERY_PARAM = "com";
    private static final String VALUE_FOR_NAME_QUERY_PARAM = "Doe";

    @LocalServerPort
    private int serverPort;
    @Autowired
    private PassengerRepository passengerRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn201AndExpectedResponse_whenSavePassenger() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(getSavePassengerRequest().build())
                .when().post(URL_PASSENGERS);

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto result = response.as(PassengerResponseDto.class);
        PassengerResponseDto expected = getAddedPassengerResponse().build();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD)
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenUpdatePassenger() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(getUpdatePassengerRequest().build())
                .when().put(URL_PASSENGERS_ID_TEMPLATE, JOHN_DOE_ID_STRING);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto result = response.as(PassengerResponseDto.class);
        PassengerResponseDto expected = getUpdatedPassengerResponse().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn204AndMinusOnePassenger_whenDeletePassenger() {

        List<Passenger> passengersBeforeDelete = passengerRepository.findAll();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete(URL_PASSENGERS_ID_TEMPLATE, JOHN_DOE_ID_STRING);

        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        List<Passenger> passengersAfterDelete = passengerRepository.findAll();

        assertThat(passengersAfterDelete).size()
                .isEqualTo(passengersBeforeDelete.size() - 1);

        Predicate<? super Passenger> matchById = p -> p.getId()
                .equals(UUID.fromString(JOHN_DOE_ID_STRING));
        assertThat(passengersBeforeDelete)
                .anyMatch(matchById);
        assertThat(passengersAfterDelete)
                .noneMatch(matchById);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPassengerById() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PASSENGERS_ID_TEMPLATE, JOHN_DOE_ID_STRING);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto result = response.as(PassengerResponseDto.class);
        PassengerResponseDto expected = getJohnDoe().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPassengerByEmail() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PASSENGERS_EMAIL_TEMPLATE, JOHN_DOE_EMAIL);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerResponseDto result = response.as(PassengerResponseDto.class);
        PassengerResponseDto expected = getJohnDoe().build();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPassengersWithNoRequestParams() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PASSENGERS);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PassengerResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<PassengerResponseDto>>() {
                });
        ListContainerResponseDto<PassengerResponseDto> expected = getListContainerForPassengers()
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
    void shouldReturn200AndExpectedResponse_whenGetPassengersWithNameEmailRequestParams() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(NAME_QUERY_PARAM, VALUE_FOR_NAME_QUERY_PARAM)
                .queryParam(EMAIL_QUERY_PARAM, VALUE_FOR_EMAIL_QUERY_PARAM)
                .when().get(URL_PASSENGERS);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PassengerResponseDto> result = response
                .as(new TypeRef<ListContainerResponseDto<PassengerResponseDto>>() {
                });
        ListContainerResponseDto<PassengerResponseDto> expected = getListContainerForPassengers()
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
