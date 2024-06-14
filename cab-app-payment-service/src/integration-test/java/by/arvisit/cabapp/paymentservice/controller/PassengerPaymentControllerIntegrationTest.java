package by.arvisit.cabapp.paymentservice.controller;

import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.PASSENGER_PAYMENT_1_ID;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.PAYMENT_METHOD_REQUEST_PARAM;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.URL_PASSENGER_PAYMENTS;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.URL_PASSENGER_PAYMENTS_ID_TEMPLATE;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getAddedPassengerPaymentResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getListContainerForResponse;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getPassengerPaymentRequestDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getPassengerPaymentResponseDto1;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getPassengerPaymentResponseDto2;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getPassengerPaymentResponseDto3;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getPassengerPaymentResponseDto4;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getPassengerResponseDto;
import static by.arvisit.cabapp.paymentservice.util.PaymentIntegrationTestData.getRideResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.common.dto.passenger.PassengerResponseDto;
import by.arvisit.cabapp.common.dto.rides.RideResponseDto;
import by.arvisit.cabapp.paymentservice.KafkaTestContainerExtension;
import by.arvisit.cabapp.paymentservice.PostgreSQLTestContainerExtension;
import by.arvisit.cabapp.paymentservice.dto.PassengerPaymentResponseDto;
import by.arvisit.cabapp.paymentservice.mock.WireMockService;
import by.arvisit.cabapp.paymentservice.persistence.model.PaymentMethodEnum;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ActiveProfiles("itest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@ExtendWith(KafkaTestContainerExtension.class)
@SqlGroup({
        @Sql(scripts = "classpath:sql/add-passenger-payments.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/delete-passenger-payments.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
@WireMockTest(httpPort = 8480)
class PassengerPaymentControllerIntegrationTest {

    private static final String[] BIG_DECIMAL_FIELDS = { "amount", "feeAmount" };
    private static final String ID_FIELD = "id";
    private static final String TIMESTAMP_FIELD = "timestamp";
    private static final String[] FIELDS_FOR_LIST_TO_IGNORE = { "amount", "feeAmount", "timestamp" };
    private static final String VALUES_FIELD = "values";

    @LocalServerPort
    private int serverPort;
    @Autowired
    private WireMockService wireMockService;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    void shouldReturn201AndExpectedResponse_whenSaveWithValidInput() throws Exception {
        PassengerResponseDto passenger = getPassengerResponseDto().build();
        wireMockService.mockResponseForPassengerClientGetPassengerById(passenger);
        RideResponseDto ride = getRideResponseDto().build();
        wireMockService.mockResponseForRideClientGetRideById(ride);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(getPassengerPaymentRequestDto().build())
                .when().post(URL_PASSENGER_PAYMENTS);

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        PassengerPaymentResponseDto actual = response.as(PassengerPaymentResponseDto.class);
        PassengerPaymentResponseDto expected = getAddedPassengerPaymentResponseDto().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD, TIMESTAMP_FIELD)
                .isEqualTo(expected);
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPaymentByIdWithExistingId() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(getPassengerPaymentRequestDto().build())
                .when().get(URL_PASSENGER_PAYMENTS_ID_TEMPLATE, PASSENGER_PAYMENT_1_ID);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PassengerPaymentResponseDto actual = response.as(PassengerPaymentResponseDto.class);
        PassengerPaymentResponseDto expected = getPassengerPaymentResponseDto1().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(BIG_DECIMAL_FIELDS)
                .ignoringFields(TIMESTAMP_FIELD)
                .isEqualTo(expected);
        assertThat(actual.timestamp())
                .isNotNull();
        assertThat(actual.amount())
                .isEqualByComparingTo(expected.amount());
        assertThat(actual.feeAmount())
                .isEqualByComparingTo(expected.feeAmount());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPaymentsWithNoRequestParams() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PASSENGER_PAYMENTS);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PassengerPaymentResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<PassengerPaymentResponseDto>>() {
                });
        ListContainerResponseDto<PassengerPaymentResponseDto> expected = getListContainerForResponse(
                PassengerPaymentResponseDto.class)
                .withValues(List.of(
                        getPassengerPaymentResponseDto1().build(),
                        getPassengerPaymentResponseDto2().build(),
                        getPassengerPaymentResponseDto3().build(),
                        getPassengerPaymentResponseDto4().build()))
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(actual.values())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(FIELDS_FOR_LIST_TO_IGNORE)
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }

    @Test
    void shouldReturn200AndExpectedResponse_whenGetPaymentsWithCashPaymentMethodRequestParam() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam(PAYMENT_METHOD_REQUEST_PARAM, PaymentMethodEnum.CASH.toString())
                .when().get(URL_PASSENGER_PAYMENTS);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PassengerPaymentResponseDto> actual = response
                .as(new TypeRef<ListContainerResponseDto<PassengerPaymentResponseDto>>() {
                });
        ListContainerResponseDto<PassengerPaymentResponseDto> expected = getListContainerForResponse(
                PassengerPaymentResponseDto.class)
                .withValues(List.of(
                        getPassengerPaymentResponseDto3().build(),
                        getPassengerPaymentResponseDto4().build()))
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(VALUES_FIELD)
                .isEqualTo(expected);

        assertThat(actual.values())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(FIELDS_FOR_LIST_TO_IGNORE)
                .containsExactlyInAnyOrderElementsOf(expected.values());
    }
}
